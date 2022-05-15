package com.unionbankng.future.authorizationserver.controllers;

import com.unionbankng.future.authorizationserver.enums.AuthProvider;
import com.unionbankng.future.authorizationserver.pojos.APIResponse;
import com.unionbankng.future.authorizationserver.pojos.RegistrationRequest;
import com.unionbankng.future.authorizationserver.services.RegistrationService;
import com.unionbankng.future.authorizationserver.services.UserService;
import com.unionbankng.future.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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

    private final MessageSource messageSource;
    private final UserService userService;
    private final RegistrationService registrationService;
    private final App app;


    @PostMapping("/v1/registration/register")
    public ResponseEntity<APIResponse> register(@Valid @RequestBody RegistrationRequest request){
        app.print("Registration Process begin");
        if(request.getAuthProvider().equals(AuthProvider.GOOGLE))
            return registrationService.googleRegistration(request);



        return registrationService.register(request);
    }

}
