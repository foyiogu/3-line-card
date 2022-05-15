package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VerfiymeWebHookApplicant {
    private String firstname;
    private String lastname;
    private String phone;
    private String idType;
    private String idNumber;
    private String middlename;
    private String photo;
    private String gender;
    private String birthdate;
}
