package com.threeline.authorizationserver.controllers;

import com.threeline.authorizationserver.pojos.ApiResponseProxy;
import com.threeline.authorizationserver.services.UserService;
import com.threeline.authorizationserver.entities.User;
import com.threeline.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
