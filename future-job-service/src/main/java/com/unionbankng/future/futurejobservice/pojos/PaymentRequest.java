package com.unionbankng.future.futurejobservice.pojos;

import lombok.*;
@Data
@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {

    protected double amount;
    protected Long proposalId;
    protected String paymentReference;
    protected String debitAccountName;
    protected String debitAccountNumber;
    protected String debitAccountType;
    protected String creditAccountName;
    protected String creditAccountNumber;
    protected String creditAccountType;
    protected String narration;
    protected String executedBy;
    protected String executedFor;
    protected  String executedByUsername;
    protected String contractReference;
}