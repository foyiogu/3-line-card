package com.unionbankng.future.paymentservice.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class PaystackAuthValidator {
    
    private static final String ALGORITHM = "HmacSHA512";
    
    public static boolean isTokenValid(String rawJsonRequest, String authToken, String secretKey) throws Exception {
    	String result = "";
    	 
        byte [] byteKey = secretKey.getBytes("UTF-8");
        SecretKeySpec keySpec = new SecretKeySpec(byteKey, ALGORITHM);
        Mac sha512_HMAC = Mac.getInstance(ALGORITHM);      
        sha512_HMAC.init(keySpec);
        byte [] mac_data = sha512_HMAC.
        doFinal(rawJsonRequest.getBytes("UTF-8"));
        result = DatatypeConverter.printHexBinary(mac_data);
        
        return result.toLowerCase().equals(authToken);
    }
    
  
}