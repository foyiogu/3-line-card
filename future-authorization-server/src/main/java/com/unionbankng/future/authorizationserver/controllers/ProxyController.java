package com.unionbankng.future.authorizationserver.controllers;

import com.unionbankng.future.authorizationserver.entities.User;
import com.unionbankng.future.authorizationserver.pojos.APIResponse;
import com.unionbankng.future.authorizationserver.pojos.ApiResponseProxy;
import com.unionbankng.future.authorizationserver.services.SecurityService;
import com.unionbankng.future.authorizationserver.services.UserService;
import com.unionbankng.future.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
public class ProxyController {

    private final UserService userService;
    private final App app;

    @GetMapping("/v1/proxy/user/get_by_id/{userId}")
    public ApiResponseProxy<User> getUserById(@PathVariable String userId) {
        app.print("ProxyController.getUserById()");

        User user = userService.findByUuid(userId).orElse(null);
        if (user != null) {
            return new ApiResponseProxy<>("Request successful", true, "01", user);
        } else {
            return new ApiResponseProxy<>("User not found", false, "02", null);
        }
    }
}
