package com.threeline.paymentservice.services;

import com.threeline.paymentservice.entities.Payment;
import com.threeline.paymentservice.enums.*;
import com.threeline.paymentservice.pojos.*;
import com.threeline.paymentservice.repositories.PaymentRepository;
import com.threeline.paymentservice.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PaymentService implements Serializable {

    private final App app;
    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Value("${spring.profiles.active}")
    private String environment;

    public APIResponse<Payment> makePayment(PaymentRequest paymentRequest) {

        app.print("Making payment");

        if((paymentRequest.getAmountPaid().compareTo(BigDecimal.valueOf(100))<0)){
            throw new IllegalArgumentException("Amount should be greater than N100");
        }

        boolean paymentSuccessful = false;

        Payment payment =  Payment.builder()
                .customerName(paymentRequest.getCustomerName())
                .customerEmail(paymentRequest.getCustomerEmail())
                .productName(paymentRequest.getProductName())
                .productId(paymentRequest.getProductId())
                .productCreatorId(paymentRequest.getProductCreatorId())
                .orderRef(paymentRequest.getOrderRef())
                .amountPaid(paymentRequest.getAmountPaid())
                .paymentReference(app.generatePaymentReference())
                .currency(paymentRequest.getCurrency() != null ? paymentRequest.getCurrency() : Currency.NGN)
                .transactionDirection(TransactionDirection.CREDIT)
                .paymentDate(new Date())
                .status(Status.PENDING)
                .settlementStatus(SettlementStatus.UNSETTLED)
                .environment(environment)
                .build();

        if(paymentRequest.getPaymentMethod().equals(PaymentMethod.CARD)){
            paymentSuccessful = processCardPayment(paymentRequest.getCardPaymentRequest());
        }else if(paymentRequest.getPaymentMethod().equals(PaymentMethod.BANK_TRANSFER)){
            paymentSuccessful = processBankTransferPayment(paymentRequest.getBankTransferPaymentRequest());
        }else if(paymentRequest.getPaymentMethod().equals(PaymentMethod.DIRECT_BANK_DEBIT)){
            paymentSuccessful = processDirectBankDebitPayment(paymentRequest.getDirectBankDebitRequest());
        }

        payment.setStatus(paymentSuccessful ? Status.SUCCESSFUL : Status.FAILED);
        paymentRepository.save(payment);

        if(!paymentSuccessful){
            return new APIResponse<>("Unable to make payment", false, null);
        }

        //Fund Wallet with Payment
        APIResponse<PaymentDTO> paymentResponse = walletService.settlePaymentToWallets(payment);
        if(paymentResponse.isSuccess()){
            payment.setSettlementStatus(SettlementStatus.SETTLED);
            paymentRepository.save(payment);
            return new APIResponse<>("Payment Successful", true, payment);
        }else{
            //TODO: Do a reversal
            payment.setStatus(Status.FAILED);
            return new APIResponse<>("Payment Failed, Reversal Triggerred", false, payment);
        }
    }

    private boolean processDirectBankDebitPayment(DirectBankDebitRequest directBankDebitRequest) {
        app.print("Processing Direct Bank Payment");
        if(directBankDebitRequest.getAccountNumber() == null || directBankDebitRequest.getAccountNumber().isEmpty()){
            throw new IllegalArgumentException("Please provide your bank account");
        }

        return probability();
    }


    private boolean processBankTransferPayment(BankTransferPaymentRequest bankTransferPaymentRequest) {
        app.print("Processing Bank Transfer");
        if(bankTransferPaymentRequest.getTransactionReference() == null || bankTransferPaymentRequest.getTransactionReference().isEmpty()){
            throw new IllegalArgumentException("Please provide your transfer transaction reference");
        }

        return probability();
    }


    private boolean processCardPayment(CardPaymentRequest cardPaymentRequest) {
        app.print("Processing Card payment");
        if(cardPaymentRequest.getCardNumber() == null || cardPaymentRequest.getCvv() == null || cardPaymentRequest.getPin() == null){
            throw new IllegalArgumentException("Can't process card payment with incomplete data");
        }
        return probability();
    }

    private boolean probability() {
        boolean[] probability = {true, true, true, true, true, true, false, true, true, true, false, true};
        int floor = (int) Math.floor(Math.random() * 12);

        return probability[floor];
    }
}