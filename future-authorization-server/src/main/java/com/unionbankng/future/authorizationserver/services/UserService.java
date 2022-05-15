package com.unionbankng.future.authorizationserver.services;

import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.unionbankng.future.authorizationserver.entities.User;
import com.unionbankng.future.authorizationserver.pojos.PersonalInfoUpdateRequest;
import com.unionbankng.future.authorizationserver.repositories.UserRepository;
import com.unionbankng.future.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final App app;


//    @Cacheable(value = "user", key = "#id")
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



    @CachePut(value = "user", key = "#userId")
    public User updatePersonalInfo(@ParameterValueKeyProvider Long userId, PersonalInfoUpdateRequest request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));


        Boolean isKeycloakPropertyChanged = !user.getLastName().equals(request.getLastName()) ||
                !user.getFirstName().equals(request.getFirstName());

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getStateOfResidence() != null)
            user.setStateOfResidence(request.getStateOfResidence());
        if (request.getAddress() != null)
            user.setUserAddress(request.getAddress());
        if (request.getCountry() != null)
            user.setCountry(request.getCountry());
        if (request.getDateOfBirth() != null)
            user.setDateOfBirth(request.getDateOfBirth());
        if (request.getDialingCode() != null)
            user.setDialingCode(request.getDialingCode());
        if (request.getPhoneNumber() != null)
            user.setPhoneNumber(request.getPhoneNumber());
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

}
