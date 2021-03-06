package com.threeline.authorizationserver.controllers;

import com.threeline.authorizationserver.enums.Role;
import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.services.UserService;
import com.threeline.authorizationserver.entities.User;
import com.threeline.authorizationserver.pojos.UserByTokenResponse;
import com.threeline.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UsersController {

    private final UserService userService;
    private final App app;

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestParam String email, @RequestParam String password) {
        User user = userService.login(email, password);
        return ResponseEntity.ok().body(new APIResponse<>("Request successful",true,user));
    }


    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse<User>> getUserById(@PathVariable Long userId) {

        User user = userService.findById(userId).orElseThrow(  ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        app.print("User found");
        return ResponseEntity.ok().body(new APIResponse<>("Request successful",true,user));
    }


    @GetMapping("/role")
    public ResponseEntity<APIResponse<User>> getUserByRole(@RequestParam Role role) {
        User user = userService.findByRole(role).orElseThrow(  ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        app.print("User found");
        return ResponseEntity.ok().body(new APIResponse<>("Request successful",true, user));
    }


    @GetMapping("/search")
    public ResponseEntity<APIResponse> getUsersBySearch(@RequestParam String  q) {
        List<User> user = userService.findUsersBySearch(q).orElseThrow(  ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok().body(new APIResponse<>("Request successful",true,user));
    }


    @GetMapping("")
    public ResponseEntity<APIResponse> getUsers(@RequestParam int  page,  @RequestParam int size) {
        Page<User> userPage = userService.findUsers(PageRequest.of(page,size));
        if(userPage.isEmpty())
            return ResponseEntity.ok().body(new APIResponse<>("No User Found",true,null));
        else
            return ResponseEntity.ok().body(new APIResponse<>("Request successful",true,userPage));
    }


    @GetMapping("/get_details_with_token")
    public ResponseEntity<APIResponse<UserByTokenResponse>> getUserByToken(@ApiIgnore OAuth2Authentication authentication) {
        User user =  userService.findByUuid(authentication.getName())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserByTokenResponse userByTokenResponse = new UserByTokenResponse();
        BeanUtils.copyProperties(user, userByTokenResponse);

        return ResponseEntity.ok().body(new APIResponse<>("Request successful",true,userByTokenResponse));
    }


}
