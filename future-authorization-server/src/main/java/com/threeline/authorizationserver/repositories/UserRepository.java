package com.threeline.authorizationserver.repositories;

import com.threeline.authorizationserver.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUuid(String uuid);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
    @Query(value = "SELECT TOP(10) * FROM app_user u  where (email like %:q% or first_name like %:q% or last_name like %:q% or phone_number like %:q%)", nativeQuery = true)
    Optional<List<User>> findUsersBySearch(String q);
}
