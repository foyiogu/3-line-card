package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class LongLivedInstagramResponse {

     private String access_token;
     private String token_type;
     private Long expires_in;
}
