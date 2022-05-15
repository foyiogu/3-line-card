package com.unionbankng.future.futurejobservice.entities;

import com.unionbankng.future.futurejobservice.enums.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Table(name="job_contract_disputes")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobContractDispute implements Serializable  {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Long id;
    @NotNull
    private  Long proposalId;
    @NotNull
    private  Long contractId;
    @NotNull
    private  Long userId;
    @NotNull
    private  Long employerId;
    @NotNull
    private  String contractReference;
    @NotNull
    private  Long jobId;
    @Column(columnDefinition="TEXT")
    private  String description;
    @Column(columnDefinition="TEXT")
    private  String referenceId;
    private String attachment;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private  Date lastModifiedDate;

    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }

    @PreUpdate
    public void setLastModifiedDate() {
        createdAt = new Date();
    }

}

