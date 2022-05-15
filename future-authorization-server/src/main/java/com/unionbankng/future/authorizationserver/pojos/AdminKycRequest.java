package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class AdminKycRequest {
    private String status;
    private String comment;
    private String verificationType;
    private String userid;
}
