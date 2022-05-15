package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.JobProposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobProposalRepository extends JpaRepository<JobProposal, Long> {
    Page<JobProposal> findAllByJobId(Pageable pageable, Long jid);
    Page<JobProposal> findByUserId(Pageable pageable, Long userid);
    @Query(value = "SELECT * FROM job_proposals p where p.user_id=:userid and status not in ('WP','PS','CO')", nativeQuery = true)
    Page<JobProposal> findProposalsByUserId(Pageable pageable, Long userid);
    @Query(value = "SELECT count(*) FROM job_proposals p where p.user_id=:userid and status not in ('WP','PS','CO')", nativeQuery = true)
    Long findNoOfProposalsByUserId(Long userid);
    @Query(value = "SELECT count(*) FROM job_proposals p where p.job_id=:jobId", nativeQuery = true)
    Long getCountByJobId(Long jobId);
    @Query(value = "SELECT top(1) * FROM job_proposals p where p.job_id=:jobId and (p.user_id=:userId or p.employer_id=:userId ) and status not in('CA','IA') order by id desc", nativeQuery = true)
    JobProposal findProposalByUserId(Long jobId, Long userId);


}
