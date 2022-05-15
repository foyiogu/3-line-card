package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordRequest{

    @NotNull
    @Size(min = 6)
    String password;
    @NotNull
    @Size(min = 6)
    String oldPassword;
    @NotNull
    Long userId;
}
