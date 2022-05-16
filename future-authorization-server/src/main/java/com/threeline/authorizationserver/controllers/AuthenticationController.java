package com.threeline.authorizationserver.controllers;
import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/v1/pin/set")
    public ResponseEntity<APIResponse> createNewPin(OAuth2Authentication authentication, @RequestParam String pin){
        return ResponseEntity.ok().body(authenticationService.createPin(authentication, pin));
    }



    @PostMapping("/v1/pin/verify")
    public ResponseEntity<APIResponse> verifyPin(OAuth2Authentication authentication, @RequestParam String pin){
        return ResponseEntity.ok().body(authenticationService.verifyPin(authentication, pin));
    }


}
