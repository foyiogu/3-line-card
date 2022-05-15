package com.unionbankng.future.paymentservice.services;

import com.unionbankng.future.paymentservice.entities.PaymentReference;
import com.unionbankng.future.paymentservice.enums.PaymentGateway;
import com.unionbankng.future.paymentservice.repositories.PaymentReferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentReferenceService {

    private final PaymentReferenceRepository paymentReferenceRepository;

    public Boolean existByPaymentGatewayAndReference(PaymentGateway paymentGateway, String ref){
        return paymentReferenceRepository.existsByPaymentGatewayAndRef(paymentGateway,ref);
    }

    public PaymentReference save(PaymentReference paymentReference){
        return paymentReferenceRepository.save(paymentReference);
    }

}
