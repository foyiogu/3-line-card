package com.threeline.paymentservice.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectBankDebitRequest {

    private String accountNumber;

}