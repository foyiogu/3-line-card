package com.threeline.authorizationserver.controllers;

import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.pojos.RegistrationRequest;
import com.threeline.authorizationserver.services.RegistrationService;
import com.threeline.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final App app;


    @PostMapping("/v1/registration/register")
    public ResponseEntity<APIResponse> register(@Valid @RequestBody RegistrationRequest request){
        app.print("Registration Process begin");
        return registrationService.register(request);
    }

}
