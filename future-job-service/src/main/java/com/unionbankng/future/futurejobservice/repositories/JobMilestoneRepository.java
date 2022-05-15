package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.JobMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobMilestoneRepository extends JpaRepository<JobMilestone,Long> {

    Optional<JobMilestone> findMilestoneByMilestoneReference(String reference);
    Optional<JobMilestone> findMilestoneByContractReference(String reference);
    @Query(value = "SELECT sum(amount) total FROM  job_milestones where contract_reference=:contractReference and  status in('CO','PS')", nativeQuery = true)
    Long findTotalSpentAmountByProposalId(String contractReference);

    @Query(value = "SELECT  * FROM  job_milestones where contract_reference=:contractReference and status in('WP','PA') order by id desc", nativeQuery = true)
    List<JobMilestone> findOngoingMilestonesByContractReference(String contractReference);

    @Query(value = "SELECT TOP(1) * FROM  job_milestones where proposal_id=:proposalId and job_id=:jobId order by id desc", nativeQuery = true)
    JobMilestone findMilestoneByProposalAndJobId(Long proposalId, Long jobId);

    @Query(value = "SELECT TOP(1) * FROM  job_milestones where proposal_id=:proposalId and employer_id=:userId order by id desc", nativeQuery = true)
    JobMilestone findMilestoneByProposalAndUserId(Long proposalId, Long userId);

    @Query(value = "SELECT * FROM  job_milestones where proposal_id=:proposalId and job_id=:jobId and status=:status order by id desc", nativeQuery = true)
    List<JobMilestone> findAllMilestonesByProposalJobAndStatus(Long proposalId, Long jobId, String status);

    @Query(value = "SELECT * FROM  job_milestones where proposal_id=:proposalId and job_id=:jobId order by id desc", nativeQuery = true)
    List<JobMilestone> findAllMilestonesByProposalAndJobId(Long proposalId, Long jobId);
}
