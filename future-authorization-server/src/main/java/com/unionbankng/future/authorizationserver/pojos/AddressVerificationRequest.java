package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class AddressVerificationRequest {
    private String firstname;
    private String surname;
    private String phone;
    private String email;
    private String dob;
    private String callbackURL;
    private String buildingNumber;
    private String street;
    private String landmark;
    private String state;
    private String city;
    private String country;
    private String image;
    private String mobile;
}
