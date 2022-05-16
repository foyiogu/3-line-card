package com.threeline.futurejobservice.controllers;

import com.threeline.futurejobservice.util.App;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "")
public class AppController {

    private final App app;

}
