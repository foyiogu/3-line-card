package com.unionbankng.future.futurejobservice.entities;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name="wallet_transactions")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Long id;
    @NotNull
    private  String  userId;
    private String executedByUsername;
    @NotNull
    private String contractReference;
    @NotNull
    private  String walletId;
    private BigDecimal totalAmountPlusCharges;
    private BigDecimal  totalAmount;
    private String currencyCode;
    private String accountType;
    private String accountBranch;
    private String accountName;
    private String accountNumber;
    private String transactionType;
    private String narration;
    private String paymentReference;
    @Temporal(TemporalType.TIMESTAMP)
    private  Date lastModifiedDate;
    @Temporal(TemporalType.TIMESTAMP)
    private   Date createdAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }

    @PreUpdate
    public void setLastModifiedDate() {
        lastModifiedDate = new Date();
    }
}
