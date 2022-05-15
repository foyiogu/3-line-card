package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.JobContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobContractRepository extends JpaRepository<JobContract, Long> {


    List<JobContract> findByJobId(Long jobId);
    Optional<JobContract> findByContractReference(String referenceId);
    @Query(value = "SELECT TOP(1) * FROM  job_contracts where proposal_id=:proposalId and job_id=:jobId order by created_at desc", nativeQuery = true)
    JobContract findContractByProposalAndJobId(Long proposalId, Long jobId);

    @Query(value = "SELECT  count(*) total_job_completed from  job_contracts c, job_proposals p where p.user_id=:userId and c.proposal_id=p.id and c.cleared_amount>0", nativeQuery = true)
    Optional<Long> getTotalJobCompletedByUser(Long userId);

    @Query(value = "SELECT  sum(c.cleared_amount) total_earning from  job_contracts c, job_proposals p where p.user_id=:userId and c.proposal_id=p.id and c.status='CO'", nativeQuery = true)
    Optional<Long> getTotalJobEarningByUserId(Long userId);

    @Query(value = "SELECT  sum(c.cleared_amount) total_expenditure from  job_contracts c, jobs j where j.oid=:userId and c.job_id=j.id", nativeQuery = true)
    Optional<Long> getTotalJobExpenditureByUserId(Long userId);



}
