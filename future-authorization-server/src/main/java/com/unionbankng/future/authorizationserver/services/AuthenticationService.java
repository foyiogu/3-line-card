
package com.unionbankng.future.authorizationserver.services;
import com.unionbankng.future.authorizationserver.entities.User;
import com.unionbankng.future.authorizationserver.pojos.*;
import com.unionbankng.future.authorizationserver.repositories.UserRepository;
import com.unionbankng.future.authorizationserver.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${encryption.key}")
    private String encryptionKey;
    @Value("${confirmation.token.minute.expiry}")
    private int tokenExpiryInMinute;
    private final UserRepository userRepository;
    private final CryptoService cryptoService;
    private final MemcachedHelperService memcachedHelperService;
    private final MessageSource messageSource;
    private final EmailSender emailSender;
    private final SMSSender smsSender;
    @Value("${forgot.pin.url}")
    private String forgotPinURL;
    @Value("${email.sender}")
    private String emailSenderAddress;
    private final App app;


    public APIResponse createPin(OAuth2Authentication authentication, String pin) {
        System.out.println("###########Creating PIN");
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        
        User user = userRepository.findByUuid(currentUser.getUserUUID()).orElse(null);
        
        if (user != null) {
            if (user.getPin() == null) {
                String encrypted = cryptoService.encrypt(pin, encryptionKey);
                if (encrypted != null) {
                    user.setPin(encrypted);
                    return new APIResponse<>("Pin Added Successfully", true, userRepository.save(user));
                } else {
                    return new APIResponse<>("Unable to Create your Transaction Pin", false, null);
                }
            } else {
                return new APIResponse<>("You Already Added a Transaction Pin to your Account", false, null);
            }
        } else {
            return new APIResponse<>("Unable to fetch authentication details", false, null);
        }
    }

    public APIResponse verifyPin(OAuth2Authentication authentication, String pin) {
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        User user = userRepository.findByUuid(currentUser.getUserUUID()).orElse(null);
        
        if (user != null) {
            if(user.getPin()!=null) {
                String existingPin = cryptoService.decrypt(user.getPin(), encryptionKey);
                String providedPin = pin;
                System.out.println("##########Pin Verification started");
                if (existingPin.equals(providedPin)) {
                    return new APIResponse<>("Verification Successful", true, user);
                } else {
                    return new APIResponse<>("Invalid Pin", false, null);
                }
            }else{
                return new APIResponse<>("No Transaction PIN set to your Account", false, null);
            }
        } else {
            return new APIResponse<>("Unable to fetch authentication details", false, null);
        }
    }


    public APIResponse generateOTP(OAuth2Authentication authentication) {
        try {
            app.print("############GENERATING OTP");
            JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
            User user = userRepository.findByUuid(currentUser.getUserUUID()).orElse(null);
            
            if (user != null) {
                String otp = this.app.generateOTP().toString();
                memcachedHelperService.save(user.getUuid(), otp, tokenExpiryInMinute * 60);

                String mobileNumber = app.toPhoneNumber(user.getPhoneNumber());
                app.print("Sending OTP.....");

                SMS sms = new SMS();
                sms.setMessage("Your OTP is " + otp);
                sms.setRecipient(mobileNumber);
                smsSender.sendSMS(sms);
                app.print("OTP sent successfully");
                return new APIResponse<>("OTP Sent Successfully", true, mobileNumber);

            } else {
                return new APIResponse<>("Unable to fetch authentication details", false, null);
            }
        } catch (Exception ex) {
            System.out.println("Unable to send OTP");
            ex.printStackTrace();
            return new APIResponse<>(ex.getMessage(), false, null);

        }
    }

    public APIResponse verifyOTP(OAuth2Authentication authentication, String otp) {

        try {
            JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
            User user = userRepository.findByUuid(currentUser.getUserUUID()).orElse(null);
            if (user != null) {
                System.out.println("Verifying OTP....");
                String memOTP = memcachedHelperService.getValueByKey(user.getUuid());
                if (memOTP.equals(otp)) {
                    return new APIResponse<>("Verification Successful", true, null);
                } else {
                    return new APIResponse<>("Invalid OTP", false, null);
                }
            } else {
                return new APIResponse<>("Unable to fetch authentication details", false, null);
            }

        } catch (Exception ex) {
            System.out.println("Unable to verify OTP");
            ex.printStackTrace();
            return new APIResponse<>(ex.getMessage(), false, null);
        }
    }


    public APIResponse resetPin(OAuth2Authentication authentication, String newPin) {

        System.out.println("###########Resetting PIN");
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);

        User user = userRepository.findByUuid(currentUser.getUserUUID()).orElse(null);
        
        if (user != null) {
            if (user.getPin() != null) {
                String encrypted = cryptoService.encrypt(newPin, encryptionKey);
                if (encrypted != null) {
                    user.setPin(encrypted);
                    return new APIResponse<>("Pin Added Successfully", true, userRepository.save(user));
                } else {
                    return new APIResponse<>("Unable to Create your Transaction Pin", false, null);
                }
            } else {
                return new APIResponse<>("You have not added a Transaction Pin to your Account", false, null);
            }
        } else {
            return new APIResponse<>("Unable to fetch authentication details", false, null);
        }
    }


    public APIResponse validatePhoneNumber(OAuth2Authentication authentication, String phone) {
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        User user = userRepository.findByUuid(currentUser.getUserUUID()).orElse(null);
        if (user != null) {
            if(phone!=null) {
                Long otp = app.generateOTP();
                String phoneNumber=app.toPhoneNumber(phone);
                memcachedHelperService.save(phoneNumber,otp.toString(),0);

                SMS smsBody= new SMS();
                smsBody.setRecipient(phoneNumber);
                smsBody.setMessage("Your OTP is " + otp);

                app.print("Sending SMS OTP:");

                smsSender.sendSMS(smsBody);
                return new APIResponse<>("OTP Sent to your phone number", true, phoneNumber);
            }else{
                return new APIResponse<>("Phone number required", false, null);
            }
        } else {
            return new APIResponse<>("Unable to fetch authentication details", false, null);
        }
    }

    public APIResponse verifyPhoneNumber(OAuth2Authentication authentication, String phone, Long otp) {
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        User user = userRepository.findByUuid(currentUser.getUserUUID()).orElse(null);

        app.print("Verifying phone number");

        String phoneNumber = app.toPhoneNumber(phone);
        String memoryOTP = memcachedHelperService.getValueByKey(phoneNumber);
        app.print("User OTP Value:");

        if (memoryOTP == null)
            return new APIResponse("OTP expired or not found", false, null);

        if(memoryOTP.equals(String.valueOf(otp).trim())) {
            user.setPhoneNumber(phoneNumber);
            memcachedHelperService.clear(phoneNumber);
            userRepository.save(user);
            return new APIResponse<>("Phone number added successfully", true, user);
        }else{
            return new APIResponse("Invalid OTP", false, null);
        }
    }
}















