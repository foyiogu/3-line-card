
package com.unionbankng.future.futurejobservice.services;
import com.unionbankng.future.futurejobservice.pojos.*;
import com.unionbankng.future.futurejobservice.entities.JobPayment;
import com.unionbankng.future.futurejobservice.retrofitservices.BankServiceInterface;
import com.unionbankng.future.futurejobservice.util.App;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UBNBankTransferService {

    private final UBNTransferResponseService jobPaymentResponseService;
    private BankServiceInterface bankServiceInterface;
    private final App app;

    @Value("${kula.bankService.baseURL}")
    private String bankServiceBaseURL;


    @PostConstruct
    public void init() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                .baseUrl(bankServiceBaseURL)
                .build();
        app.print(bankServiceBaseURL);
        bankServiceInterface = retrofit.create(BankServiceInterface.class);
    }


    public PaymentResponse transferUBNtoUBN(String authToken, JobPayment transfer) throws IOException {

        PaymentResponse apiResponseBody = new PaymentResponse();

        if(transfer.getDebitAccountType().equals("CASA")) {

            //Fetch account branch code
            UbnCustomerEnquiryRequest enquiryRequest = new UbnCustomerEnquiryRequest();
            enquiryRequest.setAccountType(transfer.getCreditAccountType());
            enquiryRequest.setAccountNumber(transfer.getDebitAccountNumber());

            app.print("Fetching branch code Request >>>");
            app.print(enquiryRequest);

            Response<APIResponse<UbnAccountEnquiryResponse>> apiResponse = bankServiceInterface.accountInquiry(authToken, enquiryRequest).execute();
            if (apiResponse.isSuccessful() && apiResponse.body().getPayload() != null) {
                UbnAccountEnquiryResponse response = apiResponse.body().getPayload();

                app.print("Response >>>");
                app.print(response);

                if (response.getAccountBranchCode() != null)
                    transfer.setDebitAccountBranchCode(response.getAccountBranchCode());
            } else {
                apiResponseBody.setCode(String.valueOf(apiResponse.code()));
                apiResponseBody.setMessage("Unable to fetch account branch code");

                return apiResponseBody;
            }
        }


        app.print("Transfer Request >>>");
        app.print(transfer);
        UBNFundTransferRequest request = new UBNFundTransferRequest();
        request.setAmount(String.valueOf(transfer.getAmount()));
        request.setCreditAccountName(transfer.getCreditAccountName());
        request.setCreditAccountBankCode("032");
        request.setCreditAccountNumber(transfer.getCreditAccountNumber());
        request.setCreditNarration(transfer.getDebitNarration());
        request.setCreditAccountBankCode("032");
        request.setCreditAccountBranchCode(transfer.getCreditAccountBranchCode() == null ? "000" : transfer.getCreditAccountBranchCode());
        request.setDebitAccountBranchCode(transfer.getDebitAccountBranchCode() == null ? "000" : transfer.getDebitAccountBranchCode());
        request.setCreditAccountType(transfer.getCreditAccountType());
        request.setCurrency(transfer.getCurrency());
        request.setDebitAccountName(transfer.getDebitAccountName());
        request.setDebitAccountNumber(transfer.getDebitAccountNumber());
        request.setDebitNarration(transfer.getDebitNarration());
        request.setDebitAccountType(transfer.getDebitAccountType());
        request.setInitBranchCode("000");
        request.setChannelCode("1");
        request.setValueDate("2020-12-04");
        request.setPaymentTypeCode("FT");
        request.setPaymentReference(transfer.getPaymentReference());


        Response<APIResponse<UBNFundTransferResponse>> apiResponse = bankServiceInterface.transferFunds(authToken, request).execute();

        if(apiResponse.isSuccessful() && apiResponse.body().getPayload()!=null) {
            apiResponseBody.setCode(apiResponse.body().getPayload().getCode());
            apiResponseBody.setMessage(jobPaymentResponseService.getResponseMessage(apiResponse.body().getPayload().getCode(), apiResponse.body().getPayload().getMessage()));
            apiResponseBody.setReference(apiResponse.body().getPayload().getReference());

        }else{
            apiResponseBody.setCode(String.valueOf(apiResponse.code()));
            apiResponseBody.setMessage(apiResponse.message());
        }
        return apiResponseBody;
    }

    public PaymentResponse transferBulkUBNtoUBN(String authToken, UBNBulkFundTransferRequest paymentRequest) throws IOException {
        app.print("Bulk Transfer Request >>>");
         //fire the request
        Response<APIResponse<UBNBulkFundTransferResponse>> apiResponse = bankServiceInterface.transferBulkFund(authToken, paymentRequest).execute();
        PaymentResponse response = new PaymentResponse();
        if(apiResponse.isSuccessful() && apiResponse.body().getPayload()!=null) {
            response.setCode(apiResponse.body().getPayload().getCode());
            response.setBatchId(apiResponse.body().getPayload().getBatchId());
            response.setMessage(jobPaymentResponseService.getResponseMessage(apiResponse.body().getPayload().getCode(), apiResponse.body().getMessage()));
            response.setCbaBatchNo(apiResponse.body().getPayload().getCbaBatchNo());
            response.setReference(apiResponse.body().getPayload().getReference());
        }else{
            response.setCode(String.valueOf(apiResponse.code()));
            response.setMessage(apiResponse.message());
        }
        return response;
    }
}

