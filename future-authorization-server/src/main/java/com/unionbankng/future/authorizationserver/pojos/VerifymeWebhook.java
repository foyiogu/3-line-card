package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VerifymeWebhook<T> {
    private String type;
    private VerifymeWebhookData data;
}
