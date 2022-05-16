package com.threeline.futurewalletservice.util;

import com.threeline.futurewalletservice.pojos.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Map;

public class JWTUserDetailsExtractor {

    public static JwtUserDetail getUserDetailsFromAuthentication(OAuth2Authentication authentication) {

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        Map<String, Object> detailsMap = (Map<String, Object>) details.getDecodedDetails();
        String userImg = detailsMap.get("userImg") == null ? null : detailsMap.get("userImg").toString();

        return JwtUserDetail.builder().
                userId(((Integer) detailsMap.get("userId")).longValue())
                .userFullName(detailsMap.get("userFullName").toString())
                .userUUID(detailsMap.get("userUUID").toString())
                .userEmail(detailsMap.get("userEmail").toString()).userImg(userImg)
                .userFullName(detailsMap.get("userFullName").toString())
                .kycLevel(detailsMap.get("kycLevel") == null ? 0 : Integer.valueOf(detailsMap.get("kycLevel").toString()))
                .walletId(detailsMap.get("walletId") == null ? null: detailsMap.get("walletId").toString())
                .isEnabled(Boolean.valueOf(detailsMap.get("isEnabled").toString()))
                .firstName(detailsMap.get("firstName").toString())
                .lastName(detailsMap.get("lastName").toString())
                .userAddress(detailsMap.get("userAddress")==null?"Unknown":detailsMap.get("userAddress").toString())
                .city(detailsMap.get("city")==null?"Unknown":detailsMap.get("city").toString())
                .zipCode(detailsMap.get("zipCode")==null?"Unknown":detailsMap.get("zipCode").toString())
                .gender(detailsMap.get("gender")==null?"Unknown":detailsMap.get("gender").toString())
                .build();
    }
}
