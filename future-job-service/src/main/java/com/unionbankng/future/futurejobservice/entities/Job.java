package com.unionbankng.future.futurejobservice.entities;
import com.unionbankng.future.futurejobservice.enums.Status;
import com.unionbankng.future.futurejobservice.enums.JobType;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name="jobs")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable=false)
    private Long oid;
    @NotNull
    @Column(columnDefinition ="TEXT", nullable=false)
    private String title;
    @NotNull
    @Column(columnDefinition ="TEXT", nullable=false)
    private String goal;
    @NotNull
    @Column(columnDefinition ="TEXT", nullable=false)
    private String description;
    private String supportingFiles;
    @NotNull
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private JobType type;
    @NotNull
    @Column(nullable=false)
    private String categories;
    private String invitationId;
    private String duration;
    @NotNull
    @Column(nullable=false)
    private Long budget;
    private String team;
    private String qualification;
    private String skillsRequired;
    private String assessment;
    private String sampleProduct;
    private boolean interview;
    private String timeline;
    private String paymentTerms;
    private String teamExpertiseLevel;
    private Long teamSize;
    @Column(name="nda_files", columnDefinition="TEXT")
    private String ndaFiles;
    @NotNull
    @Column(length=3, nullable=false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    private Boolean terms;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }

    @PreUpdate
    public void lastModifiedDate() {
        lastModifiedDate = new Date();
    }

    @Override
    public boolean equals(Object job) {
        return this.id.equals(((Job)job).getId());
    }

    @Override
    public String toString() {
        return this.title;
    }
}
