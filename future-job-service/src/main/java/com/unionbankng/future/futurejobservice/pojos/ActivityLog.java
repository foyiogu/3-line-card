package com.unionbankng.future.futurejobservice.pojos;

import com.unionbankng.future.futurejobservice.enums.LoggingOwner;
import lombok.*;

import java.io.Serializable;

public @Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
class ActivityLog implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    protected LoggingOwner owner;
    protected String requestObject;
    protected String responseObject;
    protected String userId;
    protected String username;
    protected String device;
    protected String ipAddress;
    protected String description;
}
