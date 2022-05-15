package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class Applicant {
    private String idType;
    private String idNumber;
    private String firstname;
    private String lastname;
    private String phone;
    private String dob;
}
