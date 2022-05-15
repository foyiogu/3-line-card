package com.unionbankng.future.futurejobservice.services;

import com.unionbankng.future.futurejobservice.entities.JobCategory;
import com.unionbankng.future.futurejobservice.pojos.APIResponse;
import com.unionbankng.future.futurejobservice.repositories.JobCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobCategoryService implements Serializable {

    private final JobCategoryRepository jobCategoryRepository;
    private Logger logger = LoggerFactory.getLogger(JobCategoryService.class);


    public APIResponse<JobCategory> addCategory(JobCategory jobCategory){
        if(jobCategory.getTitle()==null)
            return  new APIResponse<>("Category Title required",false,null);
        else  if(jobCategory.getTitle()==null)
            return  new APIResponse<>("Category Description required",false,null);
       else {
            List<JobCategory> existingCategory = jobCategoryRepository.findByTitle(jobCategory.getTitle()).orElse(null);
            if (existingCategory != null) {
                return new APIResponse<>("Category already exist", false, null);
            } else {
                JobCategory savedCategory = jobCategoryRepository.save(jobCategory);
                return new APIResponse<JobCategory>("success", true, savedCategory);
            }
        }
    }

    public APIResponse<List<JobCategory>> findTopCategories(){
        List<JobCategory> jobCategoryList=jobCategoryRepository.findTopCategories().orElse(null);
        if(jobCategoryList!=null)
            return new APIResponse<>("success",true,jobCategoryList);
        else
            return  new APIResponse<>("No Category Available",false,null);
    }

    public APIResponse<List<JobCategory>> findCategoryBySearch(String q){
        logger.info("Searching for ..."+q);
        List<JobCategory> jobCategoryList=jobCategoryRepository.findCategoryBySearch(q).orElse(null);
        if(jobCategoryList!=null)
            return new APIResponse<>("success",true,jobCategoryList);
        else
            return  new APIResponse<>("Search not found",false,null);
    }
}
