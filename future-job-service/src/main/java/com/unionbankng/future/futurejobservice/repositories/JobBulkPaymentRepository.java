package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.JobBulkPayment;
import com.unionbankng.future.futurejobservice.entities.JobPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobBulkPaymentRepository extends JpaRepository<JobBulkPayment,Long> {
   Optional<JobBulkPayment> findByPaymentReference(String paymentReference);
}

