package com.unionbankng.future.authorizationserver.pojos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

public @Data
@Builder
class KYCInitiationRequest implements Serializable {
    protected String bvn;
}