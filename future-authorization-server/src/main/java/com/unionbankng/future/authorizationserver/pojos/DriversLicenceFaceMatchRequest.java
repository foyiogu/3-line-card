package com.unionbankng.future.authorizationserver.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriversLicenceFaceMatchRequest {
    private String idNo;
    private String surname;
    private String firstname;
    private String dob;
    private String passportBase64String;
    private String idBase64String;

}
