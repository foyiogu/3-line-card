package com.threeline.futurewalletservice.services;

import com.threeline.futurewalletservice.enums.Role;
import com.threeline.futurewalletservice.pojos.User;
import com.threeline.futurewalletservice.repositories.WalletHistoryRepository;
import com.threeline.futurewalletservice.repositories.WalletRepository;
import com.threeline.futurewalletservice.util.App;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public User fetchUserByRole(Role role) {
        //TODO Fetch user by role from authserver
        return null;
    }

}
