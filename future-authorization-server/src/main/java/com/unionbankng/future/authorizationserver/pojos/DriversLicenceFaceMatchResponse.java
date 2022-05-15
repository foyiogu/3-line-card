package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class DriversLicenceFaceMatchResponse {
    private String message;
    private String transactionRef;
    private String status;
    private String verified;
    private String validFace;
    private String faceMatchScore;
    private String faceMatchStatus;
}
