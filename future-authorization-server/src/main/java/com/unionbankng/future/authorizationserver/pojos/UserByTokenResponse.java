package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;
import java.util.Date;

@Data
public class UserByTokenResponse {

    private Long id;
    private String img;
    private String firstName;
    private String lastName;
    private String uuid;
    private String umid;
    private String username;
    private String email;
    private String dialingCode;
    private String phoneNumber;
    private String accountNumber;
    private String accountName;
    private String address;
    private String country;
    private String stateOfResidence;
    private Date dateOfBirth;
}
