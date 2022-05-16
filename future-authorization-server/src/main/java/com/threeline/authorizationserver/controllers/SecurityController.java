package com.threeline.authorizationserver.controllers;

import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.pojos.ChangePasswordRequest;
import com.threeline.authorizationserver.pojos.ResetPasswordRequest;
import com.threeline.authorizationserver.services.SecurityService;
import com.threeline.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/security")
public class SecurityController {


    private  final SecurityService securityService;
    private final App app;

    @PostMapping("/initiate_forgot_password")
    public ResponseEntity<?> initiateForgotPassword(@RequestParam String identifier){
        app.print("Initiating Forgot password");
        return securityService.initiateForgotPassword(identifier);

    }

    @GetMapping("/confirm_reset_token")
    public ResponseEntity<APIResponse> confirmResetToken(@RequestParam String token){


        Boolean isSuccess = securityService.confirmForgotPasswordToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse("Request successful",isSuccess,null));
    }


    @PostMapping("/reset_password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request){
        app.print("Resetting password");
        return securityService.resetPassword(request.getToken(), request.getNewPassword());

    }

    @PostMapping("/change_password")
    public ResponseEntity<APIResponse> changePassword(@NotNull ChangePasswordRequest request){
        return securityService.changePassword(request);

    }


}
