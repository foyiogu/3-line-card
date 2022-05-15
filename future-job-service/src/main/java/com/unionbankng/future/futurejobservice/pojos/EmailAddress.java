package com.unionbankng.future.futurejobservice.pojos;

import com.unionbankng.future.futurejobservice.enums.RecipientType;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
public @Data class EmailAddress implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    protected String displayName;
    protected String email;
    protected RecipientType recipientType;

}