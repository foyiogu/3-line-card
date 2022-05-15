package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VotersCardFaceMatchRequest {
    private String searchParameter;
    private String country;
    private String selfie;
    private Boolean selfieToDatabaseMatch;
    private String verificationType;
}
