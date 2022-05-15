package com.unionbankng.future.futuremessagingservice.entities;
import com.unionbankng.future.futuremessagingservice.enums.Status;
import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Table(name="contactus_messages")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactUs {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    String name;
    String email;
    @Column(columnDefinition="TEXT")
    String message;
    @Column(columnDefinition="TEXT")
    String subject;
    @Enumerated(EnumType.STRING)
    Status status;
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @PrePersist
    private void setCreatedAt() {
        createdAt = new Date();
    }
}
