package com.threeline.paymentservice.controllers;

import com.threeline.paymentservice.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/test")
public class AppController {

    private final App app;

    @PostMapping("/ping")
    public ResponseEntity<?> ping() {
        app.print("Service is up and running");
        return ResponseEntity.ok().body("pong");
    }

}
