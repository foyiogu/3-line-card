package com.unionbankng.future.futurejobservice.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NoArgsConstructor
    public static @Data class EmailAttachment implements Serializable {

        private static final long serialVersionUID = -295422703255886286L;
        protected String cid;
        protected String content;
        protected boolean inline;
        protected String mime;
        protected String name;

    }
}