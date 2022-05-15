package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class AddressVerificationStatus {
    private String status;
    private String subStatus;
    private String state;
}
