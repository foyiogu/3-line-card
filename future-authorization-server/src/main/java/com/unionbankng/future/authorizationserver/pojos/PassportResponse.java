package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class PassportResponse {
    private String first_name;
    private String last_name;
    private String middle_name;
    private String dob;
    private String expiry_date;
}
