package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class IdentityBiometricsRequest {
    private String photoUrl;
    private String photo;
    private String idType;
    private String idNumber;
}
