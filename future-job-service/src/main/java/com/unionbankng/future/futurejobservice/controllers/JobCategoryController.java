package com.unionbankng.future.futurejobservice.controllers;
import com.unionbankng.future.futurejobservice.entities.JobCategory;
import com.unionbankng.future.futurejobservice.pojos.APIResponse;
import com.unionbankng.future.futurejobservice.services.JobCategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
public class JobCategoryController {

    private final JobCategoryService jobCategoryService;
    Logger logger = LoggerFactory.getLogger(JobCategoryController.class);

    @PostMapping("/v1/job/category/add")
    public ResponseEntity<APIResponse> addNewCategory(@RequestBody JobCategory jobCategory){
        return ResponseEntity.ok().body(jobCategoryService.addCategory(jobCategory));
    }
    @GetMapping("/v1/job/category/search")
    public ResponseEntity<APIResponse> findCategoryBySearch(@RequestParam String q){
        return ResponseEntity.ok().body(jobCategoryService.findCategoryBySearch(q));
    }
    @GetMapping("/v1/job/categories")
    public ResponseEntity<APIResponse> findLatestCategories(){
        return ResponseEntity.ok().body(jobCategoryService.findTopCategories());
    }
}
