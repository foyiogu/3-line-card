package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VerifymeWebhookStatus {
    private String status;
    private String subStatus;
    private String state;
}
