package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VerifyKycRequest {
    private String idType;
    private String idNumber;
    private String gender;
    private  String dob;
    private String phoneNumber;
}
