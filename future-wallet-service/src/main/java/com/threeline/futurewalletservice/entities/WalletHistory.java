package com.threeline.futurewalletservice.entities;

import com.threeline.futurewalletservice.enums.Currency;
import com.threeline.futurewalletservice.enums.Status;
import com.threeline.futurewalletservice.enums.TransactionDirection;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wallet_histories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long walletId;

    private Long userId;

    private BigDecimal amount;

    private BigDecimal accountBalance;

    private String paymentReference;

    private String orderReference;

    private String transactionReference;

    @Enumerated(EnumType.STRING)
    private TransactionDirection transactionDirection;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Status transactionStatus;


    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;


    @PrePersist
    private void setCreatedAt() {
        createdAt = new Date();
    }


    @PreUpdate
    private void setUpdatedAt() {
        dateUpdated = new Date();
    }


    @Override
    public boolean equals(Object WalletHistory) {
        return this.id.equals(((WalletHistory)WalletHistory).getId());
    }
}
