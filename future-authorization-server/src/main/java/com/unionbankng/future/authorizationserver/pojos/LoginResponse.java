package com.unionbankng.future.authorizationserver.pojos;
import com.unionbankng.future.authorizationserver.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponse {
    private String bearer;
    private User user;
}
