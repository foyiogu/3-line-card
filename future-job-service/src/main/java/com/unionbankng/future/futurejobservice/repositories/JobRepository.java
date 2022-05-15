package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    Optional<Job> findJobByInvitationId(String invitationId);
    @Query(value = "SELECT * FROM jobs j where j.oid=:id and j.status not in('IA','CA') order by id desc", nativeQuery = true)
    Page<Job> findByOid(Pageable pageable, Long id);

    @Query(value = "SELECT * FROM jobs j where j.type=:type and (j.status='AC' or (j.status='WP' and j.type='TEAMS_PROJECT')) order by id desc", nativeQuery = true)
    Page<Job> findByType(Pageable pageable, String type);

    @Query(value = "SELECT * FROM jobs j where j.type=:type and j.categories like %:category% and (j.status='AC' or (j.status='WP' and j.type='TEAMS_PROJECT')) order by id desc", nativeQuery = true)
    Page<Job> findByTypeAndCategory(Pageable pageable, String type, String category);

    @Query(value = "SELECT * FROM jobs j where j.type=:type and j.status='AC' and (j.title like %:question% or j.description like %:question% or j.categories like %:question% or j.skills_required like %:question%) order by id desc", nativeQuery = true)
    Page<Job> findBySearch(Pageable pageable, String question, String type);

    @Query(value = "SELECT * FROM jobs j where j.oid=:id and j.status=:status order by id desc", nativeQuery = true)
    Page<Job> findJobsByOwnerIdAndStatus(Pageable pageable, Long id, String status);

    @Query(value = "SELECT * FROM jobs j WHERE (j.id in(SELECT jp.job_id FROM job_proposals jp where (jp.user_id=:id or jp.employer_id=:id) and jp.status=:status)  or j.oid=:id and status=:status) order by id desc", nativeQuery = true)
    Page<Job> findJobsByUserIdAndStatus(Pageable pageable, Long id, String status);

    @Query(value = "SELECT count(*) FROM jobs j WHERE j.oid=:userId", nativeQuery = true)
    Optional<Long> getTotalJobPostedByUserId(Long userId);
}
