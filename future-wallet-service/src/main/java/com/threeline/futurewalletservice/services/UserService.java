package com.threeline.futurewalletservice.services;

import com.threeline.futurewalletservice.enums.Role;
import com.threeline.futurewalletservice.pojos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public UserDTO fetchUserByRole(Role role) {
        //TODO Fetch user by role from authserver
        return null;
    }

}
