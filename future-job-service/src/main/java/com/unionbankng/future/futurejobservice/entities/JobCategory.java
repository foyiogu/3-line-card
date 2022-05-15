package com.unionbankng.future.futurejobservice.entities;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name="job_categories")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCategory implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Long id;
    @NotNull
    private String  title;
    @Column(columnDefinition="TEXT")
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }
}
