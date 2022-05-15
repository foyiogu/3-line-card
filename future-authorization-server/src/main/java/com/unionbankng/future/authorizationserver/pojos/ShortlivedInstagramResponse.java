package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class ShortlivedInstagramResponse {

    private String access_token;
    private Long user_id;
}
