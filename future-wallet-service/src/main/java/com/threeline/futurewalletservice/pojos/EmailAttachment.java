package com.threeline.futurewalletservice.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public @Data class EmailAttachment implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    protected String cid;
    protected String content;
    protected boolean inline;
    protected String mime;
    protected String name;

}
