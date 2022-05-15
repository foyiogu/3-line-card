package com.unionbankng.future.authorizationserver.controllers;
import com.unionbankng.future.authorizationserver.pojos.APIResponse;
import com.unionbankng.future.authorizationserver.pojos.ResetPinRequest;
import com.unionbankng.future.authorizationserver.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/v1/pin/set")
    public ResponseEntity<APIResponse> createNewPin(OAuth2Authentication authentication, @RequestParam String pin){
        return ResponseEntity.ok().body(authenticationService.createPin(authentication, pin));
    }


    @PostMapping("v1/pin/reset-pin")
    public ResponseEntity<?> resetPin(OAuth2Authentication authentication, @RequestBody @Valid ResetPinRequest request){
        return ResponseEntity.ok().body(authenticationService.resetPin(authentication, request.getNewPin()));
    }

    @PostMapping("/v1/pin/verify")
    public ResponseEntity<APIResponse> verifyPin(OAuth2Authentication authentication, @RequestParam String pin){
        return ResponseEntity.ok().body(authenticationService.verifyPin(authentication, pin));
    }


    @PostMapping("/v1/otp/generate")
    public ResponseEntity<APIResponse> generateOTP(OAuth2Authentication authentication){
        return ResponseEntity.ok().body(authenticationService.generateOTP(authentication));
    }


    @PostMapping("/v1/otp/verify")
    public ResponseEntity<APIResponse> verifyOTP(OAuth2Authentication authentication, @RequestParam String otp){
        return ResponseEntity.ok().body(authenticationService.verifyOTP(authentication, otp));
    }


    @PostMapping("/v1/verification/validate_phonenumber")
    public ResponseEntity<APIResponse> validatePhoneNumber(OAuth2Authentication authentication, @RequestParam String phone){
        return ResponseEntity.ok().body(authenticationService.validatePhoneNumber(authentication, phone));
    }

    @PostMapping("/v1/verification/verify_phonenumber")
    public ResponseEntity<APIResponse> verifyPhoneNumber(OAuth2Authentication authentication, @RequestParam String phone, @RequestParam Long otp){
        return ResponseEntity.ok().body(authenticationService.verifyPhoneNumber(authentication, phone, otp));
    }


}
