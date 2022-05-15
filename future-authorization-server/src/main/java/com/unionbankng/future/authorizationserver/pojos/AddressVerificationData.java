package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class AddressVerificationData {
    private String id;
    private AddressVerificationApplicant applicant;
    private String createdAt;
    private String completedAt;
    private String lattitude;
    private String longitude;
    private AddressVerificationNeighbor neighbor;
    private AddressVerificationStatus status;
    private String city;
    private String street;
    private String lga;
    private String state;
    private String country;
    private String reference;
}
