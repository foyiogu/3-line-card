package com.threeline.futurewalletservice.repositories;

import com.threeline.futurewalletservice.entities.Wallet;
import com.threeline.futurewalletservice.enums.Currency;
import com.threeline.futurewalletservice.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Boolean existsByAccountNumber(String accountNumber);

    Optional<Wallet> findByAccountNumber(String accountNumber);

    Optional<Wallet> findByUserId(Long userId);

    @Query("select w from Wallet w where w.userId = ?1 and w.currency = ?2")
    Optional<Wallet> findByUserIdAndCurrency(Long userId, Currency currency);

    boolean existsByUserId(Long userId);

    Optional<Wallet> findByRole(Role clientInstitution);
}
