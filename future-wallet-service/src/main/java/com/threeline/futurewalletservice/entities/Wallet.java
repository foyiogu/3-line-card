package com.threeline.futurewalletservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.threeline.futurewalletservice.enums.Currency;
import com.threeline.futurewalletservice.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String userUuid;

    private String accountName;

    private String userEmail;

    @Column(scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    private boolean isBlocked;

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
    public boolean equals(Object wallet) {
        return this.id.equals(((Wallet)wallet).getId());
    }
}
