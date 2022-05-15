package com.unionbankng.future.futurejobservice.entities;
import com.unionbankng.future.futurejobservice.enums.ConfigReference;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="configs")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Config implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String uid;
    @Enumerated(EnumType.STRING)
    private ConfigReference reference;
    private String type;
    private String value;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt =new Date();
    }
}
