package com.unionbankng.future.futurejobservice.entities;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name="job_transfers")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPayment  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String executedByUsername;
    private String  executedBy;
    private  String  executedFor;
    @NotNull
    private String contractReference;
    private double amount;
    @NotNull
    private String creditAccountName;
    @NotNull
    private String creditAccountBankCode;
    @NotNull
    private String creditAccountNumber;
    @NotNull
    private  String creditNarration;
    @NotNull
    private String creditAccountBranchCode;
    @NotNull
    private String debitAccountBranchCode;
    private String creditAccountType;
    private String currency;
    @NotNull
    private String debitAccountName;
    @NotNull
    private String debitAccountNumber;
    private  String debitNarration;
    private String debitAccountType;
    private String initBranchCode;
    private String initialPaymentReference;
    private  String paymentReference;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }

}
