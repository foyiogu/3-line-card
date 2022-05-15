package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class TokenConfirm {
    private Boolean success;
    private Long userId;
}
