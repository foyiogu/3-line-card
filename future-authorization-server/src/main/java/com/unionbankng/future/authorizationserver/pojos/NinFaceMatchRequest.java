package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class NinFaceMatchRequest {
    private String idNo;
    private String idbase64String;
    private String firstname;
    private String surname;
    private String dob;
    private String passportBase64String;
}
