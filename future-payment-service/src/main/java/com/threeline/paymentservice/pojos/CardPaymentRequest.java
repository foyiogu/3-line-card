package com.threeline.paymentservice.pojos;

import com.threeline.paymentservice.enums.Currency;
import com.threeline.paymentservice.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentRequest {

    private String cardNumber;

    private String cvv;

    private String pin;

}