package com.unionbankng.future.futurejobservice.entities;
import com.unionbankng.future.futurejobservice.enums.Status;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name="job_team_details")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobTeamDetails  implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Long id;
    @NotNull
    private  Long proposalId;
    @NotNull
    private  Long userId;
    @NotNull
    private  Long employerId;
    @NotNull
    private  String fullName;
    private  String img;
    private  String email;
    @NotNull
    private  Long jobId;
    private   Long amount;
    private Long percentage;
    @Temporal(TemporalType.TIMESTAMP)
    private  Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(columnDefinition="TEXT")
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private  Date createdAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }

}

