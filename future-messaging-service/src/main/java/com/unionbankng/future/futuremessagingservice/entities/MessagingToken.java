package com.unionbankng.future.futuremessagingservice.entities;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name="messaging_tokens")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagingToken {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    @NotNull
    Long userId;
    @NotNull
    @Column(columnDefinition="TEXT")
    String token;
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    public MessagingToken(MessagingToken notification) {
        this.id = notification.id;
        this.userId=notification.userId;
        this.token=notification.token;
        this.createdAt = notification.createdAt;
    }

    @PrePersist
    private void setCreatedAt() {
        createdAt = new Date();
    }
}
