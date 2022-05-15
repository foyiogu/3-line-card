package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class PassportFaceMatchRequest {
    private String transactionReference;
    private String searchParameter;
    private String firstName;
    private String lastName;
    private String gender;
    private String dob;
    private String verificationType;
    private String email;
    private String phone;
    private String selfie;
    private String selfieToDatabaseMatch;
}
