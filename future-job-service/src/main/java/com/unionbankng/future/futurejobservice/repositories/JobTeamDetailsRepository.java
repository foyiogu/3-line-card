package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.JobTeamDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobTeamDetailsRepository extends JpaRepository<JobTeamDetails,Long> {

    JobTeamDetails findByProposalId(Long proposalId);
    List<JobTeamDetails> findByJobId(Long jobId);
}
