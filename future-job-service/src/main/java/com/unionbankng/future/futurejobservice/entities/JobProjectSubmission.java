package com.unionbankng.future.futurejobservice.entities;

import com.unionbankng.future.futurejobservice.enums.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name="job_contract_submission")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobProjectSubmission  implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private  Long id;
    @NotNull
    private  Long proposalId;
    @NotNull
    private  Long userId;
    @NotNull
    private  Long employerId;
    @NotNull
    private Long jobId;
    private  String link;
    @NotNull
    private  String contractReference;
    private String supportingFiles;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(columnDefinition="TEXT")
    private  String description;
    @Column(columnDefinition="TEXT")
    private  String remark;
    private  String lastModifiedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private  Date lastModifiedDate;
    @Temporal(TemporalType.TIMESTAMP)
    private  Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date rejectedDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedDate;

    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }


}
