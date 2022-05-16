package com.threeline.futurewalletservice.pojos;

import com.threeline.futurewalletservice.enums.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    private Long id;

    private String customerName;

    private String customerEmail;

    private String productName;

    private Long productId;

    private Long productCreatorId;

    private String orderRef;

    private BigDecimal amountPaid;

    private String paymentReference;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private TransactionDirection transactionDirection;

    private Date paymentDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private SettlementStatus settlementStatus;

}
