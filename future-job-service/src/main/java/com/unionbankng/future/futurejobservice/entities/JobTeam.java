package com.unionbankng.future.futurejobservice.entities;
import com.unionbankng.future.futurejobservice.enums.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name="job_teams")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobTeam implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long jobId;
    @NotNull
    private String invitationId;
    @Column(columnDefinition="TEXT")
    private   String selectedTeam;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @Temporal(TemporalType.TIMESTAMP)
    private   Date createdAt;


    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }
}
