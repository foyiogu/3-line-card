package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory,Long> {
    Optional<List<JobCategory>> findByTitle(String title);
    @Query(value="SELECT * FROM job_categories c WHERE (c.title like %:search% or c.description like %:search%)",nativeQuery = true)
    Optional<List<JobCategory>> findCategoryBySearch(String search);
    @Query(value = "SELECT TOP(40) * FROM job_categories c ORDER BY c.id DESC", nativeQuery = true)
    Optional<List<JobCategory>> findTopCategories();

}
