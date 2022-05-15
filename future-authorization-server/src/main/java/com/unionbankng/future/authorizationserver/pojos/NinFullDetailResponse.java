package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

import java.util.List;

@Data
public class NinFullDetailResponse<T> {
    private String responseCode;
    private String description;
    private String verificationType;
    private String verificationStatus;
    private String transactionStatus;
    private String transactionReference;
    private String transactionDate;
    private String searchParameter;
    private List<T> response;
    private List<T> faceMatch;
}
