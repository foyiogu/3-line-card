package com.threeline.paymentservice.controllers;

import com.threeline.paymentservice.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final App app;

    @GetMapping("/test")
    public String test() {
        System.out.println("test");
        return "Hello World";
    }
}
