package com.unionbankng.future.futurejobservice.controllers;
import com.unionbankng.future.futurejobservice.entities.Job;
import com.unionbankng.future.futurejobservice.enums.JobType;
import com.unionbankng.future.futurejobservice.pojos.*;
import com.unionbankng.future.futurejobservice.repositories.JobRepository;
import com.unionbankng.future.futurejobservice.services.JobService;
import com.unionbankng.future.futurejobservice.services.UserService;
import com.unionbankng.future.futurejobservice.util.App;
import com.unionbankng.future.futurejobservice.util.JWTUserDetailsExtractor;
import com.unionbankng.future.futurejobservice.util.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
public class JobController {

    private final App app;
    private final JobService service;

    @PostMapping(value="/v1/job/add", consumes="multipart/form-data")
    public ResponseEntity<APIResponse> addJob(OAuth2Authentication authentication, @Valid @RequestParam(value = "data", required=true) String jobData,
                                              @RequestParam(value = "team", required=true) String teamData,
                                              @RequestParam(value = "supportingFiles", required = false) MultipartFile[] supportingFiles,
                                              @RequestParam(value = "ndaFiles", required = false) MultipartFile[] ndaFiles) throws IOException{

            return ResponseEntity.ok().body(service.addJob(authentication,jobData,teamData,supportingFiles,ndaFiles));
    }

    @PutMapping("/v1/job/close")
    public  ResponseEntity<APIResponse> closeJobById(OAuth2Authentication authentication,@RequestParam Long id, @RequestParam int state){
        Job job=service.closeJobById(authentication, id,state);
        if(job!=null)
           return ResponseEntity.ok().body(new APIResponse("Job closed successful",true,job));
        else
            return ResponseEntity.ok().body(new APIResponse("Unable to close the job",false,null));
    }


    @PutMapping("/v1/job/repeat")
    public  ResponseEntity<APIResponse> repeatJobById(@RequestParam Long id,OAuth2Authentication authentication){
        Job job=service.repeatJobById(authentication,id);
        if(job!=null)
            return ResponseEntity.ok().body(new APIResponse("Job repeated successful",true,job));
        else
            return ResponseEntity.ok().body(new APIResponse("Unable to repeat the job",false,null));
    }

    @DeleteMapping("/v1/job/delete/{id}")
    public ResponseEntity<APIResponse> deleteJob(OAuth2Authentication authentication, @PathVariable  Long id){
        service.deleteJobById(authentication,id);
        return ResponseEntity.ok().body(new APIResponse("Job deleted successful",true,null));
    }

    @GetMapping("/v1/job/{id}")
    public ResponseEntity<APIResponse> getJobById(@PathVariable Long id, Model model){
        return ResponseEntity.ok().body(
                new APIResponse("success",true,service.findJobById(id,model)));
    }
    @GetMapping("/v1/total/job/posted/by/{id}")
    public ResponseEntity<APIResponse> getTotalJobPostedByUser(@PathVariable Long id){
        return ResponseEntity.ok().body(
                new APIResponse("success",true,service.getTotalJobPostedByUserId(id)));
    }
    @GetMapping("/v1/job/invitation/{invitationId}")
    public ResponseEntity<APIResponse> getJobByInvitationId(@PathVariable String invitationId, Model model){
        return ResponseEntity.ok().body(
                new APIResponse("success",true,service.getJobByInvitationId(invitationId,model)));
    }

    @GetMapping("/v1/jobs/owner/{oid}")
    public ResponseEntity<APIResponse> getJobsByOwnerId(@PathVariable Long oid,@RequestParam int page, @RequestParam int size, Model model){
        return ResponseEntity.ok().body(
                new APIResponse("success",true,service.findJobsByOwnerId(oid,PageRequest.of(page,size), model)));
    }
    @GetMapping("/v1/user/jobs/status/{uid}")
    public ResponseEntity<APIResponse> getJobsByUserIdAndStatus(@PathVariable Long uid,@RequestParam String status, @RequestParam int page, @RequestParam int size, Model model){
        return ResponseEntity.ok().body(
                new APIResponse("success",true,service.findJobsByUserIdAndStatus(uid,status,PageRequest.of(page,size), model)));
    }
    @GetMapping("/v1/my-job/list/{oid}")
    public ResponseEntity<APIResponse> getJobsByOwnerIdAndStatus(@PathVariable Long oid,@RequestParam String status, @RequestParam int page, @RequestParam int size, Model model){
        return ResponseEntity.ok().body(
                new APIResponse("success",true,service.findJobsByOwnerIdAndStatus(oid,status,PageRequest.of(page,size), model)));
    }

    @GetMapping("/v1/jobs/get_by_type/{type}")
    public ResponseEntity<APIResponse<Model>> getJobsByType(@PathVariable String type, @RequestParam int page, @RequestParam int size, Model model){
        return ResponseEntity.ok().body(
                new APIResponse("success",true,service.findJobsByType(JobType.valueOf(type.toUpperCase()),PageRequest.of(page,size), model)));
    }

    @GetMapping("/v1/jobs/get_by_type_and_category/{type}")
    public ResponseEntity<APIResponse<Model>> findJobsByTypeAndCategory(@PathVariable String type,@RequestParam String  category, @RequestParam int page, @RequestParam int size, Model model){
        return ResponseEntity.ok().body(
                new APIResponse("success",true,service.findJobsByTypeAndCategory(JobType.valueOf(type.toUpperCase()),category,PageRequest.of(page,size), model)));
    }
    @GetMapping("/v1/jobs/search/{type}")
    public ResponseEntity<APIResponse<Model>> searchJobs(@RequestParam String q, @PathVariable String type, @RequestParam int page, @RequestParam int size, Model model){
        app.print(app);
        return ResponseEntity.ok().body(
                new APIResponse("success",true,service.searchJobs(JobType.valueOf(type.toUpperCase()),q,PageRequest.of(page,size), model)));
    }

    @GetMapping("/v1/jobs")
    public ResponseEntity<APIResponse<Model>> getAnyJob(@RequestParam int page, @RequestParam int size, Model model){
        return ResponseEntity.ok().body(
                new APIResponse("success",true,service.getJobs(PageRequest.of(page,size), model)));
    }

}
