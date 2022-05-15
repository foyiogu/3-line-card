package com.unionbankng.future.paymentservice.pojos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class VerifyTransactionResponse {

    /**
     * this status is "true" if the request is successful and false if is not
     * NOTE: This does not mean the transaction was successful, data.status holds that information
     */
    private boolean status;
    /**
     * information about the request, could be "verification successful" or "invalid key"
     */
    private String message;
    /**
     * contains details about the transaction
     */
    private PaystackData data;


}