package com.threeline.authorizationserver.controllers;

import com.threeline.authorizationserver.enums.Role;
import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.pojos.RegistrationRequest;
import com.threeline.authorizationserver.services.RegistrationService;
import com.threeline.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path ="/registration" )
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final App app;


    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@Valid @RequestBody RegistrationRequest request){
        app.print("Registration Process begin");
        return registrationService.register(request, Role.CONTENT_CREATOR);
    }

    @GetMapping("/foo")
    public ResponseEntity<String> testRegistration(){
        app.print("Confirmation Process begin");
        return ResponseEntity.ok("test");
    }

}
