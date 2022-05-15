package com.unionbankng.future.futurejobservice.services;

import com.unionbankng.future.futurejobservice.pojos.UBNBulkFundTransferRequest;
import com.unionbankng.future.futurejobservice.pojos.UBNBulkFundTransferBatchItem;
import com.unionbankng.future.futurejobservice.entities.*;
import com.unionbankng.future.futurejobservice.pojos.*;
import com.unionbankng.future.futurejobservice.repositories.*;
import com.unionbankng.future.futurejobservice.util.App;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JobPaymentService implements Serializable {

    private Logger logger = LoggerFactory.getLogger(JobPaymentService.class);
    private final UBNBankTransferService bankTransferService;
    private final JobPaymentRepository jobPaymentRepository;
    private final JobBulkPaymentRepository jobBulkPaymentRepository;
    private final App app;

  public APIResponse makePayment(String authToken,PaymentRequest paymentRequest) {

      //transfer back the charged amount to the Employer
      JobPayment payment = new JobPayment();
      payment.setAmount(paymentRequest.getAmount());
      payment.setCurrency("NGN");
      payment.setPaymentReference(paymentRequest.getPaymentReference());
      payment.setExecutedBy(paymentRequest.getExecutedBy());
      payment.setExecutedByUsername(paymentRequest.getExecutedByUsername());
      payment.setExecutedFor(paymentRequest.getContractReference());
      payment.setInitBranchCode("000");
      //debit the escrow amount
      payment.setDebitAccountName(paymentRequest.getDebitAccountName());
      payment.setDebitAccountNumber(paymentRequest.getDebitAccountNumber());
      payment.setDebitAccountBranchCode("000");
      payment.setDebitAccountType(paymentRequest.getDebitAccountType());
      payment.setDebitNarration(paymentRequest.getNarration());
      //credit the employer account
      payment.setCreditAccountName(paymentRequest.getCreditAccountName());
      payment.setCreditAccountNumber(paymentRequest.getCreditAccountNumber());
      payment.setCreditAccountBankCode("032");
      payment.setCreditAccountBranchCode("000");
      payment.setCreditAccountType(paymentRequest.getCreditAccountType());
      payment.setCreditNarration(paymentRequest.getNarration());

      app.print(payment);
      PaymentResponse transferResponse = null;
      try {
          transferResponse = bankTransferService.transferUBNtoUBN(authToken,payment);
          app.print(transferResponse);
      }catch (Exception ex){
          ex.printStackTrace();
      }
      if(transferResponse!=null) {
          if (transferResponse.getCode().compareTo("00") == 0) {
              //save the payment history
              payment.setInitialPaymentReference(paymentRequest.getPaymentReference());
              payment.setPaymentReference(transferResponse.getReference());
              payment.setContractReference(paymentRequest.getContractReference());
              jobPaymentRepository.save(payment);
              logger.info("JOBSERVICE: Payment successful");
              return new APIResponse("Payment Successful", true, transferResponse.getReference());
          } else {
              String remark = transferResponse.getCode() + ": Payment Failed " + transferResponse.getMessage();
              logger.info("JOBSERVICE: Payment failed");
              logger.error(transferResponse.getMessage());
              logger.error(transferResponse.getCode());
              return new APIResponse(remark, false, transferResponse);
          }
      }else{
          String remark = "Payment Failed ";
          logger.info("JOBSERVICE: Payment failed");
          return new APIResponse(remark, false, null);
      }
  }
    public APIResponse makeBulkPayment(String authToken, ArrayList<JobBulkPayment> bulkPaymentBatchItems){

        ArrayList<UBNBulkFundTransferBatchItem> batchItems= new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String referenceId="BULK"+app.makeUIID().substring(0,5);

        for(JobBulkPayment batchItem: bulkPaymentBatchItems) {
            UBNBulkFundTransferBatchItem bank = new UBNBulkFundTransferBatchItem();
            bank.setAccountNumber(batchItem.getAccountNumber());
            bank.setAccountType(batchItem.getAccountType());
            bank.setAccountName(batchItem.getAccountName());
            bank.setAccountBranchCode("000");
            bank.setAccountBankCode("032");
            bank.setNarration(batchItem.getNarration());
            bank.setInstrumentNumber("");
            bank.setAmount(String.valueOf(batchItem.getAmount()));
            bank.setValueDate(simpleDateFormat.format(new Date()));
            bank.setCrDrFlag(batchItem.getCrDrFlag());
            bank.setFeeOrCharges("false");
            bank.setTransactionId(batchItem.getTransactionId());
            batchItems.add(bank);
        }
        UBNBulkFundTransferRequest request =new  UBNBulkFundTransferRequest();
        request.setCurrency("NGN");
        request.setPaymentReference(referenceId);
        request .setInitBranchCode("000");
        request .setTransactionDate(simpleDateFormat.format(new Date()));
        request.setPaymentTypeCode("FT");
        request .setExternalSystemReference("");
        request .setBatchItems(batchItems);

        PaymentResponse transferResponse = null;
        try {
           transferResponse = bankTransferService.transferBulkUBNtoUBN(authToken,request);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(transferResponse!=null) {
            if (transferResponse.getCode().compareTo("00") == 0) {
                for (JobBulkPayment batchItem : bulkPaymentBatchItems) {
                    batchItem.setInitialPaymentReference(referenceId);
                    batchItem.setPaymentReference(transferResponse.getReference());
                    batchItem.setAccountBranchCode("000");
                    batchItem.setAccountBankCode("032");
                    batchItem.setFeeOrCharges("false");
                    batchItem.setInstrumentNumber("");
                }
                jobBulkPaymentRepository.saveAll(bulkPaymentBatchItems);
                logger.info("JOBSERVICE: Payment successful");
                return new APIResponse("Payment Successful", true, transferResponse.getReference());
            } else {
                String remark = transferResponse.getCode() + ": Payment Failed " + transferResponse.getMessage();
                logger.info("JOBSERVICE: Payment failed");
                logger.error(transferResponse.getMessage());
                logger.error(transferResponse.getCode());
                return new APIResponse(remark, false, transferResponse);
            }
        }else{
            String remark = "Payment Failed ";
            logger.info("JOBSERVICE: Payment failed");
            return new APIResponse(remark, false, null);
        }
    }
}

