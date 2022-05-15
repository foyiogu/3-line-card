package com.unionbankng.future.futurejobservice.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.unionbankng.future.futurejobservice.entities.Job;
import com.unionbankng.future.futurejobservice.entities.JobProposal;
import com.unionbankng.future.futurejobservice.entities.JobTeamDetails;
import com.unionbankng.future.futurejobservice.enums.Status;
import com.unionbankng.future.futurejobservice.pojos.APIResponse;
import com.unionbankng.future.futurejobservice.services.JobContractService;
import com.unionbankng.future.futurejobservice.services.JobProposalService;
import com.unionbankng.future.futurejobservice.services.JobTeamDetailsService;
import com.unionbankng.future.futurejobservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
public class JobProposalController {

    private final JobTeamDetailsService jobTeamDetailsService;
    private final JobContractService contractService;
    private final JobProposalService service;
    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(JobProposalController.class);

    @PostMapping(value = "/v1/job/apply", consumes = "multipart/form-data")
    public ResponseEntity<APIResponse> applyJob(@Valid @RequestParam(value = "data", required = true) String proposalData,

                                                             @RequestParam(value = "supportingFiles", required = false) MultipartFile[] supportingFiles,@ApiIgnore OAuth2Authentication authentication) throws JsonProcessingException {
        JobProposal appliedJob = service.applyJob(authentication,proposalData, supportingFiles);
        if (appliedJob != null) {
            logger.info("Success");
            return ResponseEntity.ok().body(new APIResponse("success", true, appliedJob));
        }
        else {
            logger.info("Failed");
            return ResponseEntity.ok().body(new APIResponse("failed", false, null));
        }
    }

    @GetMapping("/v1/job/proposal/{id}")
    public ResponseEntity<APIResponse> getProposalById(@PathVariable Long id, Model model) {
        return ResponseEntity.ok().body(
                new APIResponse("success", true, service.findProposalById(id, model)));
    }


    @GetMapping("/v1/user/job/proposal/{jobId}/{userId}")
    public ResponseEntity<APIResponse> getProposalByUserId(@PathVariable Long jobId, @PathVariable Long userId, Model model) {
        return ResponseEntity.ok().body(
                new APIResponse("success", true, service.findProposalByUserId(jobId, userId, model)));
    }

    @GetMapping("/v1/job/proposals/{jid}")
    public ResponseEntity<APIResponse<Optional<List<JobProposal>>>> getJobsByJobId(@PathVariable Long jid, @RequestParam int page, @RequestParam int size, Model model) {
        return ResponseEntity.ok().body(
                new APIResponse("success", true, service.findProposalsByJobId(jid, PageRequest.of(page, size), model)));
    }

    @GetMapping("/v1/jobs/applied/{id}")
    public ResponseEntity<APIResponse<List<Job>>> getAppliedJobByUserId(@PathVariable Long id, @RequestParam int page, @RequestParam int size, Model model) {
        return ResponseEntity.ok().body(
                new APIResponse("success", true, service.findAppliedJobsByUserId(id, PageRequest.of(page, size), model)));
    }

    @GetMapping("/v1/jobs/count/applied/{id}")
    public ResponseEntity<APIResponse<Long>> getNoOfAppliedJobByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                new APIResponse("success", true, service.noOfAppliedJobsByUserId(id)));
    }

    @GetMapping("/v1/job/proposals/count/{jid}")
    public ResponseEntity<APIResponse<Long>> getProposalsCountByJobId(@PathVariable Long jid) {
        return ResponseEntity.ok().body(
                new APIResponse("success", true, service.getProposalsCount(jid)));
    }

    @PutMapping("/v1/job/proposal/status")

    public ResponseEntity<APIResponse> updateProposalStatusById(@RequestParam Long id, @RequestParam String status) {
        return ResponseEntity.ok().body(
                new APIResponse("success", true, service.updateJobStatus(id, Status.valueOf(status))));
    }

    @PostMapping("/v1/job/proposal/approve")
    public ResponseEntity<APIResponse> approveJobProposal(@RequestHeader(value="Authorization") String authorization, @Valid @RequestBody String approvalRequest, Model model, @ApiIgnore OAuth2Authentication authentication) throws JsonProcessingException {
        APIResponse response = contractService.approveJobProposal(authorization,authentication, approvalRequest);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/v1/job/proposal/cancel")
    public ResponseEntity<APIResponse> cancelJobProposal(OAuth2Authentication authentication, @RequestParam Long jid, @RequestParam Long uid) {
        JobProposal canceledProposal = service.cancelJobProposal(authentication,jid, uid);
        if (canceledProposal != null)
            return ResponseEntity.ok().body(
                    new APIResponse("success", true, canceledProposal));
        else
            return ResponseEntity.ok().body(
                    new APIResponse("failed", false, null));
    }

    @PutMapping("/v1/job/proposal/decline/{proposalId}")

    public ResponseEntity<APIResponse> declineJobProposal(OAuth2Authentication authentication, @PathVariable Long proposalId) {
        JobProposal declinedProposal = service.declineJobProposal(authentication,proposalId);
        if (declinedProposal != null)
            return ResponseEntity.ok().body(
                    new APIResponse("success", true, declinedProposal));
        else
            return ResponseEntity.ok().body(
                    new APIResponse("failed", false, null));
    }



    @PutMapping("/v1/job/proposal/change/percentage")
    public ResponseEntity<APIResponse> changePercentage(@RequestParam Long pid, @RequestParam int percentage) {
        JobProposal updateProposal = service.changeProposalPercentage(pid, percentage);
        if (updateProposal != null)
            return ResponseEntity.ok().body(
                    new APIResponse("success", true, updateProposal));
        else
            return ResponseEntity.ok().body(
                    new APIResponse("failed", false, null));
    }


    @GetMapping("/v1/job/teams/{jobId}")
    public ResponseEntity<APIResponse> findTeamsByJobId(@PathVariable Long jobId){
        List<JobTeamDetails> response= jobTeamDetailsService.findTeamsByJobId(jobId);
        if(response!=null)
            return ResponseEntity.ok().body(new APIResponse("success",true, response));
        else
            return ResponseEntity.ok().body(new APIResponse("failed",false, null));
    }

}

