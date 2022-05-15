package com.unionbankng.future.authorizationserver.security;

import com.unionbankng.future.authorizationserver.entities.User;
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
        additionalInfo.put("userImg", user.getImg());
        additionalInfo.put("userFullName", user.getFirstName()+" "+user.getLastName());
        additionalInfo.put("isEnabled", user.getIsEnabled());
        additionalInfo.put("firstName", user.getFirstName());
        additionalInfo.put("lastName", user.getLastName());
        additionalInfo.put("authProvider", user.getAuthProvider());
        additionalInfo.put("kycLevel", user.getKycLevel());
        additionalInfo.put("userAddress", user.getUserAddress());
        additionalInfo.put("city", user.getCity());
        additionalInfo.put("gender", user.getGender());
        additionalInfo.put("walletId", user.getWalletId());
        additionalInfo.put("zipCode", user.getZipCode());


        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }


}