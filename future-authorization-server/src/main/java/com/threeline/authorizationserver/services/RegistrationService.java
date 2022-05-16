package com.threeline.authorizationserver.services;

import com.threeline.authorizationserver.controllers.RegistrationController;
import com.threeline.authorizationserver.enums.Role;
import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.entities.User;
import com.threeline.authorizationserver.pojos.ErrorResponse;
import com.threeline.authorizationserver.pojos.RegistrationRequest;
import com.threeline.authorizationserver.security.PasswordValidator;
import com.threeline.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final MessageSource messageSource;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final App app;

    private PasswordValidator passwordValidator  = PasswordValidator.
            buildValidator(false, true, true, 6, 40);

    public ResponseEntity register(RegistrationRequest request){

        app.print("####REGISTERING USER####");
        if (userService.existsByEmail(request.getEmail()) || userService.existsByPhoneNumber(request.getPhoneNumber())) {

            app.print("@@@@@@User already exist");
            User existingUser = null;
                    existingUser = userService.findByEmail(request.getEmail()).orElse(
                            userService.findByPhoneNumber(request.getPhoneNumber()).orElse(null)
            );

            app.print("User found: ");
            ErrorResponse errorResponse = new ErrorResponse();

            assert existingUser != null;

            if(existingUser.getIsEnabled()) {
                errorResponse.setCode("00");
                errorResponse.setRemark(messageSource.getMessage("account.active", null, LocaleContextHolder.getLocale()));
            }
            else {
                //send confirmation email
                errorResponse.setCode("01");
                errorResponse.setRemark(messageSource.getMessage("account.inactive", null, LocaleContextHolder.getLocale()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new APIResponse(errorResponse.getRemark(), false, errorResponse));
        }

        if(!passwordValidator.validatePassword(request.getPassword()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new APIResponse(messageSource.getMessage("password.validation.error", null, LocaleContextHolder.getLocale()),false,null));

        //    private String walletId;
        //    private String walletAccountNumber;
        //    private String gender;


        // generate uuid for user
        String generatedUuid = app.makeUIID();
        User user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .kycLevel(1).role(Role.CONTENT_CREATOR)
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail()).phoneNumber(
                        app.validNumber(request.getPhoneNumber()) ? request.getPhoneNumber() : null).isEnabled(Boolean.TRUE)
                .uuid(generatedUuid).password(passwordEncoder.encode(request.getPassword())).gender(request.getGender()).build();

        user = userService.save(user);
        app.print("Saved user");

        createWalletForUser(user);

        return ResponseEntity.ok().body(
                new APIResponse(messageSource.getMessage("success.registration.message", null, LocaleContextHolder.getLocale()),true, user.getId()));

    }

    private void createWalletForUser(User user) {
        app.print("Creating wallet for user");

    }

}
