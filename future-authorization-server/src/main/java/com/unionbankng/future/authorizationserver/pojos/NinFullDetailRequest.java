package com.unionbankng.future.authorizationserver.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NinFullDetailRequest {
    private String searchParameter;
    private String verificationType;
    private String transactionReference;
}
