package com.threeline.authorizationserver.controllers;

import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/test")
public class AppController {

    private final SecurityService securityService;

    @PostMapping("/email")
    public ResponseEntity<APIResponse<?>> testForgotPassword(@RequestParam("email") String email) {
        securityService.initiateForgotPassword(email);
        return ResponseEntity.ok().body(new APIResponse<>("Request Successful",true,null));
    }

    @PostMapping("/ping")
    public ResponseEntity<APIResponse<?>> ping(@RequestParam("message") String message) {
        return ResponseEntity.ok().body(new APIResponse<>("Responding to you message:" + message,true,null));
    }

}
