package com.threeline.futurewalletservice.pojos;

import com.threeline.futurewalletservice.enums.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PaymentDTO {

    private Long id;

    private String customerName;

    private String customerEmail;

    private String productName;

    private Long productId;

    private Long productCreatorId;

    private String orderRef;

    private BigDecimal amountPaid;

    private String paymentReference;

    private PaymentMethod paymentMethod;

    private Currency currency;

    private TransactionDirection transactionDirection;

    private Date paymentDate;

    private Status status;

    private SettlementStatus settlementStatus;

}
