package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class VerifymeWebhookData {
    private String id;
    private VerfiymeWebHookApplicant applicant;
    private String createdAt;
    private String completedAt;
    private String lattitude;
    private String comment;
    private String agentSubmittedAt;
    private String longitude;
    private String city;
    private String street;
    private String lga;
    private String state;
    private String country;
    private String reference;
    private VerifymeWebhookStatus status;
    private VerifymeWebhookNeighbor neighbor;


}
