package com.unionbankng.future.futuremessagingservice.repositories;
import com.unionbankng.future.futuremessagingservice.entities.MessagingToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagingTokenRepository extends JpaRepository<MessagingToken,Long> {
    MessagingToken findTokenByUserId(Long userId);
}
