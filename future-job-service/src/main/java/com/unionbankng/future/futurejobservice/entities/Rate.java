package com.unionbankng.future.futurejobservice.entities;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name="job_freelancer_rating")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rate  implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
   private  Long id;
    @NotNull
    private   Long userId;
    @NotNull
    private  Long rate;
    @NotNull
    private   String description;
    @Column(columnDefinition="TEXT")
    private   String feedback;
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
