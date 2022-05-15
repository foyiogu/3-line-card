package com.unionbankng.future.paymentservice.repositories;

import com.unionbankng.future.paymentservice.entities.PaymentReference;
import com.unionbankng.future.paymentservice.enums.PaymentGateway;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentReferenceRepository  extends JpaRepository<PaymentReference,Long> {

    Boolean existsByPaymentGatewayAndRef(PaymentGateway paymentGateway, String ref);
}
