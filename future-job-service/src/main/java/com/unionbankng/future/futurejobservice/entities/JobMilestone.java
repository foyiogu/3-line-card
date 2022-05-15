package com.unionbankng.future.futurejobservice.entities;
import com.unionbankng.future.futurejobservice.enums.Status;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Table(name="job_milestones")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobMilestone  implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long proposalId;
    @NotNull
    private  Long userId;
    @NotNull
    private  Long employerId;
    @NotNull
    private  Long contractId;
    @NotNull
    private  Long jobId;
    @NotNull
    private  double amount;
    private double clearedAmount;
    private double charges;
    private double vat;
    @Temporal(TemporalType.TIMESTAMP)
    private   Date startDate;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private   Date endDate;
    @NotNull
    @Column(columnDefinition="TEXT")
    private  String title;
    private String supportingFiles;
    @NotNull
    @Column(columnDefinition="TEXT")
    @NotNull
    private  String milestoneReference;
    private  String contractReference;
    private  String description;
    private  String initialPaymentReferenceA;
    private  String settlementPaymentReferenceA;
    private  String reversalPaymentReferenceA;
    private  String initialPaymentReferenceB;
    private String settlementPaymentReferenceB;
    private String reversalPaymentReferenceB;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @Temporal(TemporalType.TIMESTAMP)
    private  Date createdAt;


    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }

}

