package com.threeline.futurewalletservice.pojos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

public @Data
@Builder
class EmailBody implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    protected List<EmailAttachment> attachments;
    protected String body;
    protected String footer;
    protected List<EmailAddress> recipients;
    protected EmailAddress sender;
    protected String subject;

}