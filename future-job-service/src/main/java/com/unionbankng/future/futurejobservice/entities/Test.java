package com.unionbankng.future.futurejobservice.entities;

import com.unionbankng.future.futurejobservice.enums.JobType;
import com.unionbankng.future.futurejobservice.enums.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name="test")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Test implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String data;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }

}
