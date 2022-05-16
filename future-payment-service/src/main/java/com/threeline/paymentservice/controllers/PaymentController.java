package com.threeline.paymentservice.controllers;

import com.threeline.paymentservice.entities.Payment;
import com.threeline.paymentservice.pojos.APIResponse;
import com.threeline.paymentservice.pojos.PaymentRequest;
import com.threeline.paymentservice.services.PaymentService;
import com.threeline.paymentservice.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Body;

import javax.validation.Valid;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final App app;
    private final PaymentService paymentService;

    @PostMapping("/pay")
    public APIResponse<Payment> makePayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        app.print("making payment...");
        return paymentService.makePayment(paymentRequest);
    }
}
