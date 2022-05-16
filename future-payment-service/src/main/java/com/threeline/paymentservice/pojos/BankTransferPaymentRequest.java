package com.threeline.paymentservice.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankTransferPaymentRequest {

    private String transactionReference;

}