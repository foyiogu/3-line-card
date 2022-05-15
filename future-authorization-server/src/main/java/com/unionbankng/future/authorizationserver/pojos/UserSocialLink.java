package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

import java.util.List;

@Data
public class UserSocialLink {

    private Long userId;

    private String twitter;

    private String LinkedIn;

    private String dribble;

    private String behance;

    private String github;

    private String website;

}
