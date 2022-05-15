package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRateRepository extends JpaRepository<Rate,Long> {

    @Query(value = "SELECT sum(rate) rating FROM job_freelancer_rating l where l.user_id=:userId", nativeQuery = true)
    Optional<Long> getTotalFreRatesByUser(Long userId);
    Rate findByUserId(Long userId);
}
