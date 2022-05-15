package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data
public class ThirdPartyOauthResponse {

    private String email;
    private String firstName;
    private String lastName;
    private String image;
}
