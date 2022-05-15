package com.unionbankng.future.futurejobservice.controllers;
import com.unionbankng.future.futurejobservice.pojos.APIResponse;
import com.unionbankng.future.futurejobservice.services.JobFreelancerRateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
public class JobFreelancerRateController {

    private final JobFreelancerRateService jobFreelancerRateService;
    Logger logger = LoggerFactory.getLogger(JobFreelancerRateController.class);

    @GetMapping("/v1/total/freelancer/rates/by/{id}")
    public ResponseEntity<APIResponse> getTotalFreelancerRatesByUser(@PathVariable Long id){
        return ResponseEntity.ok().body(
                new APIResponse("success",true,jobFreelancerRateService.getTotalFreRatesByUser(id)));
    }

}
