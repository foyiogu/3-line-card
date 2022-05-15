package com.unionbankng.future.authorizationserver.entities;
import com.unionbankng.future.authorizationserver.enums.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="auth_history")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length=50, nullable = false)
    private String name;
    private String username;
    private String location;
    private String device;
    private String ipAddress;
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date loginDate;

    @Override
    public String toString() {
        return this.name;
    }
}
