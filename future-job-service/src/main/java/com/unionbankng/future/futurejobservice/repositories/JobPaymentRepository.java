package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.JobPayment;
import com.unionbankng.future.futurejobservice.entities.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPaymentRepository extends JpaRepository<JobPayment,Long> {
   Optional<JobPayment> findByPaymentReference(String paymentReference);
}

