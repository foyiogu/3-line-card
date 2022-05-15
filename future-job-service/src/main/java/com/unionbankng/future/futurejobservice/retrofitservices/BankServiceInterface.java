package com.unionbankng.future.futurejobservice.retrofitservices;
import com.unionbankng.future.futurejobservice.pojos.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface BankServiceInterface {

    @POST("bankserv/api/v1/ubn_funds/transfer")
    @Headers({
            "Accept: application/json"
    })
    Call<APIResponse<UBNFundTransferResponse>> transferFunds(@Header("Authorization") String authorization, @Body UBNFundTransferRequest request);

    @POST("bankserv/api/v1/ubn_funds/bulk_transfer")
    @Headers({
            "Accept: application/json"
    })
    Call<APIResponse<UBNBulkFundTransferResponse>> transferBulkFund(@Header("Authorization") String authorization, @Body UBNBulkFundTransferRequest request);

    @GET("bankserv/api/v1/ubn/account_inquiry")
    @Headers({
            "Accept: application/json"
    })
    Call<APIResponse<UbnAccountEnquiryResponse>> accountInquiry(@Header("Authorization") String authorization, @Body UbnCustomerEnquiryRequest request);


}
