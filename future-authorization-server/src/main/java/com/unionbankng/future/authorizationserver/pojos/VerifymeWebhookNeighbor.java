package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VerifymeWebhookNeighbor {
    private Boolean isAvailable;
    private String name;
    private String comment;
    private String phone;
}
