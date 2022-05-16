package com.threeline.authorizationserver.services;

import com.threeline.authorizationserver.entities.User;
import com.threeline.authorizationserver.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<List<User>> findUsersBySearch(String question) {
        return userRepository.findUsersBySearch(question);
    }


    public Page<User> findUsers(Pageable  pageable) {
        return userRepository.findAll(pageable);
    }


    @Cacheable(value = "user", key = "#uuId")
    public Optional<User> findByUuid(String uuId) {
        return userRepository.findByUuid(uuId);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public User login(String email, String password) {
        //Todo: implement login
        return null;
    }
}
