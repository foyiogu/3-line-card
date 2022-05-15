package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VotersCardFaceMatchResponse<T> {
    private String responseCode;
    private String description;
    private String verificationType;
    private String verificationStatus;
    private String transactionStatus;
    private String transactionReference;
    private String transactionDate;
    private String searchParameter;
    private VotersCardResponse response;
    private FaceMatchResponse faceMatch;
}
