package com.threeline.authorizationserver.services;

import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.threeline.authorizationserver.entities.User;
import com.threeline.authorizationserver.repositories.UserRepository;
import com.threeline.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
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
    private final App app;


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


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }


    @CacheEvict(value = "user", key = "#userId")
    public void deleteById(@ParameterValueKeyProvider Long userId) {
        userRepository.deleteById(userId);
    }


    public Optional<User> findByEmailOrUsername(@ParameterValueKeyProvider String email, @ParameterValueKeyProvider String username) {
        return userRepository.findByEmailOrUsername(email, username);
    }


    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }


}
