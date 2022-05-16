package com.threeline.futurewalletservice.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

public @Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
class EmailMessage implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    protected List<EmailAttachment> attachments;
    protected String body;
    protected String subject;
    protected EmailAddress sender;
    protected String recipient;
}