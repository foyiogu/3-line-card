package com.unionbankng.future.paymentservice.interfaces;

import org.springframework.http.ResponseEntity;

public interface IPayment {

    Boolean validatePayment(String... args);
    ResponseEntity<String> completePayment(Object request);

}
