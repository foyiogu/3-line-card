package com.unionbankng.future.futurejobservice.services;

import com.unionbankng.future.futurejobservice.entities.WalletTransaction;
import com.unionbankng.future.futurejobservice.pojos.*;
import com.unionbankng.future.futurejobservice.repositories.JobWalletTransactionRepository;
import com.unionbankng.future.futurejobservice.util.App;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JobWalletPaymentService implements Serializable {

    private Logger logger = LoggerFactory.getLogger(JobPaymentService.class);
    private final WalletService walletService;
    private final JobWalletTransactionRepository walletTransactionRepository;
    private final App app;

    public APIResponse debitWallet(JwtUserDetail currentUser,PaymentRequest paymentRequest) {
        //transfer back the charged amount to the Employer
        WalletDebitRequest request = new WalletDebitRequest();
        request.setTotalAmount(BigDecimal.valueOf(paymentRequest.getAmount()));
        request.setTotalAmountPlusCharges(BigDecimal.valueOf(paymentRequest.getAmount()));
        request.setCurrencyCode("NGN");
        request.setRef(paymentRequest.getPaymentReference());
        request.setNaration(paymentRequest.getNarration());
        request.setWalletId(currentUser.getWalletId());
        request.setCreditAccountName(paymentRequest.getDebitAccountName());
        request.setCreditAccountNumber(paymentRequest.getDebitAccountNumber());
        request.setCreditAccountType(paymentRequest.getDebitAccountType());
        request.setCreditAccountBranch("000");
        request.setTransactionType("DEBIT");
        APIResponse<WalletDebitCreditResponse> paymentResponse = walletService.debitWallet(request);

        if (paymentResponse.isSuccess()) {

            WalletTransaction transaction = new WalletTransaction();
            transaction.setTotalAmount(BigDecimal.valueOf(paymentRequest.getAmount()));
            transaction.setTotalAmountPlusCharges(BigDecimal.valueOf(paymentRequest.getAmount()));
            transaction.setCurrencyCode("NGN");
            transaction.setPaymentReference(paymentRequest.getPaymentReference());
            transaction.setNarration(paymentRequest.getNarration());
            transaction.setWalletId(currentUser.getWalletId());
            transaction.setAccountName(paymentRequest.getDebitAccountName());
            transaction.setAccountNumber(paymentRequest.getDebitAccountNumber());
            transaction.setAccountType(paymentRequest.getDebitAccountType());
            transaction.setAccountBranch("000");
            transaction.setTransactionType("DEBIT");
            transaction.setContractReference(paymentRequest.getContractReference());
            transaction.setUserId(currentUser.getUserUUID());
            transaction.setExecutedByUsername(paymentRequest.getExecutedByUsername());
            transaction.setCreatedAt(new Date());
            //save the payment history
            walletTransactionRepository.save(transaction);
            logger.info("JOBSERVICE: Payment successful via wallet");
            return new APIResponse("Payment Successful", true, transaction.getPaymentReference());
        } else {
            logger.info("JOBSERVICE: Payment failed");
            return new APIResponse(paymentResponse.getMessage(), false, null);
        }
    }

    public APIResponse creditWallet(JwtUserDetail currentUser,PaymentRequest paymentRequest) {
        //transfer back the charged amount to the Employer
        WalletCreditRequest request = new WalletCreditRequest();
        request.setTotalAmount(BigDecimal.valueOf(paymentRequest.getAmount()));
        request.setTotalAmountPlusCharges(BigDecimal.valueOf(paymentRequest.getAmount()));
        request.setCurrencyCode("NGN");
        request.setRef(paymentRequest.getPaymentReference());
        request.setNaration(paymentRequest.getNarration());
        request.setWalletId(currentUser.getWalletId());
        request.setDebitAccountName(paymentRequest.getDebitAccountName());
        request.setDebitAccountNumber(paymentRequest.getDebitAccountNumber());
        request.setDebitAccountType(paymentRequest.getDebitAccountType());
        request.setDebitAccountBranch("000");
        request.setTransactionType("DEBIT");
        APIResponse<WalletDebitCreditResponse> paymentResponse = walletService.creditWallet(request);
        if (paymentResponse.isSuccess()) {

            WalletTransaction transaction = new WalletTransaction();
            transaction.setTotalAmount(BigDecimal.valueOf(paymentRequest.getAmount()));
            transaction.setTotalAmountPlusCharges(BigDecimal.valueOf(paymentRequest.getAmount()));
            transaction.setCurrencyCode("NGN");
            transaction.setPaymentReference(paymentRequest.getPaymentReference());
            transaction.setNarration(paymentRequest.getNarration());
            transaction.setWalletId(currentUser.getWalletId());
            transaction.setAccountName(paymentRequest.getDebitAccountName());
            transaction.setAccountNumber(paymentRequest.getDebitAccountNumber());
            transaction.setAccountType(paymentRequest.getDebitAccountType());
            transaction.setAccountBranch("000");
            transaction.setTransactionType("CREDIT");
            transaction.setContractReference(paymentRequest.getContractReference());
            transaction.setUserId(currentUser.getUserUUID());
            transaction.setExecutedByUsername(paymentRequest.getExecutedByUsername());
            transaction.setCreatedAt(new Date());
            //save the payment history
            walletTransactionRepository.save(transaction);
            logger.info("JOBSERVICE: Payment successful via wallet");
            return new APIResponse("Payment Successful", true, transaction.getPaymentReference());
        } else {
            logger.info("JOBSERVICE: Payment failed");
            return new APIResponse(paymentResponse.getMessage(), false, null);
        }
    }
}

