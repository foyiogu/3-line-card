package com.threeline.futurewalletservice.repositories;

import com.threeline.futurewalletservice.entities.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Long> {

}
