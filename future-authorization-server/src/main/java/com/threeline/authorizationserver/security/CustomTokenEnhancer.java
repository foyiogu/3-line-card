package com.threeline.authorizationserver.security;

import com.threeline.authorizationserver.entities.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();

        additionalInfo.put("userId", user.getId());
        additionalInfo.put("userUUID", user.getUuid());
        additionalInfo.put("userEmail", user.getEmail());
        additionalInfo.put("userPhone", user.getPhoneNumber());
        additionalInfo.put("userFullName", user.getFirstName()+" "+user.getLastName());
        additionalInfo.put("isEnabled", user.getIsEnabled());
        additionalInfo.put("firstName", user.getFirstName());
        additionalInfo.put("lastName", user.getLastName());
        additionalInfo.put("kycLevel", user.getKycLevel());
        additionalInfo.put("gender", user.getGender());
        additionalInfo.put("walletId", user.getWalletId());
        additionalInfo.put("walletAccountNumber", user.getWalletAccountNumber());


        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }


}