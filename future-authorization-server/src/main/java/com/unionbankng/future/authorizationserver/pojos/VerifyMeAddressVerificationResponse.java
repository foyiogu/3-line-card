package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VerifyMeAddressVerificationResponse {
    private String status;
    private AddressVerificationData data;
}
