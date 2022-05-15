package com.unionbankng.future.futuremessagingservice.interfaces;

import com.unionbankng.future.futuremessagingservice.pojos.EmailBody;

public interface EmailProvider {

    void init();
    void send(EmailBody email) throws Exception;
}
