package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VerifyMeResponseDataPhotoMatching {
    private String match;
    private String matchScore;
    private String matchingThreshold;
    private String maxScore;
}
