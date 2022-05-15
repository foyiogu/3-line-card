package com.unionbankng.future.futurejobservice.entities;

import com.unionbankng.future.futurejobservice.enums.ChatFileUploadStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="chat_files")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatFile implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String fileId;
    @Column(columnDefinition ="TEXT", nullable=false)
    private String link;
    @Column(columnDefinition ="TEXT", nullable=false)
    private String name;
    private String type;
    private Long size;
    private ChatFileUploadStatus status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt = new Date();
    }
}
