package com.threeline.paymentservice.pojos;
import com.threeline.paymentservice.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class PaymentRequest {

    private String customerName;

    private String customerEmail;

    private String productName;

    @NotNull
    private Long productId;

    private Long productCreatorId;

    private String orderRef;

    @NotNull
    private BigDecimal amountPaid;

    @NotNull
    private PaymentMethod paymentMethod;

    private Currency currency;

    private BankTransferPaymentRequest bankTransferPaymentRequest;

    private CardPaymentRequest cardPaymentRequest;

    private DirectBankDebitRequest directBankDebitRequest;


}