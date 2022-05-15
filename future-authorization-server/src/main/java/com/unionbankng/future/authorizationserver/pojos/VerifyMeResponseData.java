package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VerifyMeResponseData {
    private String firstname;
    private String lastname;
    private String middlename;
    private String birthdate;
    private VerifyMeResponseDataPhotoMatching photoMatching;
}
