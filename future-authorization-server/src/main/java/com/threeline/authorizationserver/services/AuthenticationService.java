
package com.threeline.authorizationserver.services;
import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.pojos.JwtUserDetail;
import com.threeline.authorizationserver.repositories.UserRepository;
import com.threeline.authorizationserver.entities.User;
import com.threeline.authorizationserver.utils.CryptoService;
import com.threeline.authorizationserver.utils.JWTUserDetailsExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${encryption.key}")
    private String encryptionKey;
    private final UserRepository userRepository;
    private final CryptoService cryptoService;


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

}















