package com.threeline.futurewalletservice.repositories;

import com.threeline.futurewalletservice.entities.Wallet;
import com.threeline.futurewalletservice.entities.WalletHistory;
import com.threeline.futurewalletservice.enums.Currency;
import com.threeline.futurewalletservice.enums.WalletOwnerRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Long> {

}
