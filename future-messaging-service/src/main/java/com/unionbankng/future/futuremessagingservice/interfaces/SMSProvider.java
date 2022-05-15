package com.unionbankng.future.futuremessagingservice.interfaces;

import com.unionbankng.future.futuremessagingservice.pojos.SMS;

public interface SMSProvider {

    void init();
    void send(SMS sms) throws Exception;
}
