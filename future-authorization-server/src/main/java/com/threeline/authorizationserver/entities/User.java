package com.threeline.authorizationserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.threeline.authorizationserver.enums.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length=32, nullable = false)
    private String firstName;

    @Column(length=32, nullable = false)
    private String lastName;

    @Column(length=50, nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String walletId;

    @JsonIgnore
    private String pin;

    @Column(length=32, unique = true)
    private String phoneNumber;

    @Column(length = 15)
    private String walletAccountNumber;


    private String gender;

    @JsonIgnore
    @Column(length = 5)
    private int kycLevel;

    @JsonIgnore
    @Column
    private Role role = Role.CONTENT_CREATOR;

    @JsonIgnore
    @Column(nullable = false)
    private Boolean isEnabled = true;


    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;

    public User(User user){
        this.isEnabled = user.getIsEnabled();
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.uuid = user.getUuid();
        this.email = user.getEmail();
        this.phoneNumber=user.getPhoneNumber();
        this.password = user.getPassword();
        this.kycLevel=user.getKycLevel();
        this.gender=user.getGender();
        this.walletId=user.getWalletId();
        this.walletAccountNumber = user.getWalletAccountNumber();
        this.role = user.getRole();
    }

    @PrePersist
    private void setCreatedAt() {
        createdAt = new Date();
    }


    @PreUpdate
    private void setUpdatedAt() {
        dateUpdated = new Date();
    }


    @Override
    public boolean equals(Object user) {
        return this.id.equals(((User)user).getId());
    }


    @Override
    public String toString() {
        return this.firstName+" "+this.lastName;
    }
}
