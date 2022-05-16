package com.threeline.futurewalletservice.controllers;

import com.threeline.futurewalletservice.util.App;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/test")
public class AppController {

    private final App app;

    @GetMapping(path = "/ping")
    public String ping() {
        app.print("pinging the service");
        return "pong";
    }

}
