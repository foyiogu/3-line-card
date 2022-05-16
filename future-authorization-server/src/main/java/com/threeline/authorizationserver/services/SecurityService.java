package com.threeline.authorizationserver.services;

import com.threeline.authorizationserver.enums.RecipientType;
import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.entities.User;
import com.threeline.authorizationserver.pojos.ChangePasswordRequest;
import com.threeline.authorizationserver.pojos.EmailAddress;
import com.threeline.authorizationserver.pojos.EmailBody;
import com.threeline.authorizationserver.security.PasswordValidator;
import com.threeline.authorizationserver.utils.App;
import com.threeline.authorizationserver.utils.EmailSender;
import com.threeline.authorizationserver.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final MemcachedHelperService memcachedHelperService;
    private final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private final MessageSource messageSource;
    private final EmailSender emailSender;
    private final PasswordEncoder encoder;
    private final App app;
    private final PasswordValidator passwordValidator = PasswordValidator.
            buildValidator(false, true, true, 6, 40);

    @Value("${forgot.token.seconds.expiry}")
    private int tokenExpiryInMinute;
    @Value("${forgot.password.url}")
    private String forgotPasswordURL;

    public ResponseEntity<?> initiateForgotPassword(String identifier){

        app.print("#########Initiating forgot password");
        Optional<User> emailOwner = userService.findByEmailOrUsername(identifier,identifier);

        if(emailOwner.isEmpty()){
            return ResponseEntity.ok().body(new APIResponse<>("This email is not registered", false, null));
        }
        User user = emailOwner.get();

        String token = UUID.randomUUID().toString();

        app.print("#### Password Reset");

        memcachedHelperService.save(token,user.getEmail(),0);
        String generatedURL = String.format("%s?token=%s",forgotPasswordURL,token);

        app.print("Reset Password Token:");

        String emailSenderAddress = "info@3lineng.com";
        EmailBody emailBody = EmailBody.builder().body(messageSource.getMessage("forgot.password", new String[]{generatedURL,
                Utility.convertMinutesToWords(tokenExpiryInMinute)}, LocaleContextHolder.getLocale())
        ).sender(EmailAddress.builder().displayName("Kula Team").email(emailSenderAddress).build()).subject("Reset Your Kula Password")
                .recipients(Arrays.asList(EmailAddress.builder().recipientType(RecipientType.TO).
                        email(user.getEmail()).displayName(user.toString()).build())).build();

        emailSender.sendEmail(emailBody);

        APIResponse<String> response = new APIResponse<>("Password Reset Email Sent", true, null);

        return ResponseEntity.ok().body(response);
    }

    public Boolean confirmForgotPasswordToken(String token){
        String userEmail = memcachedHelperService.getValueByKey(token);
        return userEmail != null;

    }

    public ResponseEntity<?> resetPassword(String token, String password){
        app.print("Resetting user password");

        String userEmail = memcachedHelperService.getValueByKey(token);

        if(userEmail == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new APIResponse("Token expired or not found",false,null));

        if(!passwordValidator.validatePassword(password))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new APIResponse(messageSource.getMessage("password.validation.error",
                            null, LocaleContextHolder.getLocale()),false,null));


        User user  = userService.findByEmail(userEmail).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        user.setPassword(encoder.encode(password));
        user.setIsEnabled(Boolean.TRUE);

        userService.save(user);
        memcachedHelperService.clear(token);

        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse("Password reset successful",true,null));


    }

    public ResponseEntity<APIResponse> changePassword(ChangePasswordRequest request){

        if(!passwordValidator.validatePassword(request.getPassword()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new APIResponse<>(messageSource.getMessage("password.validation.error",
                            null, LocaleContextHolder.getLocale()),false,null));

        if (request.getPassword().equals(request.getOldPassword()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new APIResponse("New password and old password can't be the same",false,null));

        User user = userService.findById(request.getUserId())
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        if (!encoder.matches(request.getOldPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    new APIResponse("You have entered an incorrect old password",false,null));


        user.setPassword(encoder.encode(request.getPassword()));
        userService.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse("Request Successful",true,null));
    }
}
