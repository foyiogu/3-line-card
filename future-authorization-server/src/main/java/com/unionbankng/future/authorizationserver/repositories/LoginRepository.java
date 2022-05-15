package com.unionbankng.future.authorizationserver.repositories;
import com.unionbankng.future.authorizationserver.entities.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {
}