package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VerifyMeResponse {
    private String status;
    private String code;
    private String message;
    private VerifyMeResponseData data;

}
