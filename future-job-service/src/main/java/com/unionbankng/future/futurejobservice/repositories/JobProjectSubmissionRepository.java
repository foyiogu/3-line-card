package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.JobProjectSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobProjectSubmissionRepository extends JpaRepository<JobProjectSubmission,Long> {

    @Query(value = "SELECT TOP(1) * FROM  job_contract_submission where contract_reference=:contractReference order by id desc", nativeQuery = true)
     JobProjectSubmission findSubmittedProjectByContractReference(String contractReference);

    @Query(value = "SELECT TOP(1) * FROM  job_contract_submission where proposal_id=:proposalId and job_id=:jobId order by id desc", nativeQuery = true)
    JobProjectSubmission findSubmittedProjectByProposalAndJobId(Long proposalId, Long jobId);

    @Query(value = "SELECT TOP(1) * FROM  job_contract_submission where proposal_id=:proposalId and (employer_id=:userId or user_id=:userId) order by id desc", nativeQuery = true)
    JobProjectSubmission findSubmittedProjectByProposalAndUserId(Long proposalId, Long userId);
}
