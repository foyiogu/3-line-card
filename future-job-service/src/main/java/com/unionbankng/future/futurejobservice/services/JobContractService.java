package com.unionbankng.future.futurejobservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.unionbankng.future.futurejobservice.entities.*;
import com.unionbankng.future.futurejobservice.enums.ConfigReference;
import com.unionbankng.future.futurejobservice.enums.PaymentMethod;
import com.unionbankng.future.futurejobservice.enums.Status;
import com.unionbankng.future.futurejobservice.enums.JobType;
import com.unionbankng.future.futurejobservice.pojos.*;
import com.unionbankng.future.futurejobservice.repositories.*;
import com.unionbankng.future.futurejobservice.util.App;
import com.unionbankng.future.futurejobservice.util.AppLogger;
import com.unionbankng.future.futurejobservice.util.JWTUserDetailsExtractor;
import com.unionbankng.future.futurejobservice.util.NotificationSender;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JobContractService implements Serializable {

    @Value("${sidekiq.escrow.appId}")
    private  String  appId;
    @Value("${sidekiq.escrow.token}")
    private  String token;
    @Value("${sidekiq.escrow.baseUrl}")
    private  String baseURL;
    private  String escrowAccountName; //GL
    private  String escrowAccountNumber;
    private  String kulaIncomeAccountName;//GL
    private  String kulaIncomeAccountNumber;
    private  String VATAccountName; //GL
    private  String VATAccountNumber;
    private  String pepperestIncomeAccountName; //CASA
    private  String pepperestIncomeAccountNumber;
    private MessageSource messageSource;
    private  Logger logger = LoggerFactory.getLogger(JobContractService.class);


    @Autowired
    private RestTemplate rest;
    private final JobRepository jobRepository;
    private final JobContractRepository jobContractRepository;
    private final ConfigService configService;
    private final JobContractExtensionRepository jobContractExtensionRepository;
    private final JobProposalRepository jobProposalRepository;
    private final JobProjectSubmissionRepository jobProjectSubmissionRepository;
    private final JobMilestoneRepository jobMilestoneRepository;
    private final JobRateRepository jobRateRepository;
    private final FileStoreService fileStoreService;
    private final JobPaymentService jobPaymentService;
    private final  JobWalletPaymentService jobWalletPaymentService;
    private final JobTeamDetailsRepository jobTeamDetailsRepository;
    private final JobContractDisputeRepository jobContractDisputeRepository;
    private final NotificationSender notificationSender;
    private  final UserService userService;
    private final AppLogger appLogger;
    private final App app;



    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setCacheControl("no-cache");
        headers.add("Token", token);
        return headers;
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static long decodeDuration(long duration, String type) {
        long[] factor = {1, 7, 30, 360};
        switch (type) {
            case "D":
                return factor[0] * duration;
            case "W":
                return factor[1] * duration;
            case "M":
                return factor[2] * duration;
            case "Y":
                return factor[3] * duration;
            default:
                return duration;
        }
    }

    public Long getTotalJobEarningByUserId(Long userId) {
        return jobContractRepository.getTotalJobEarningByUserId(userId).orElse(0l);
    }

    public Long getTotalJobExpenditureByUser(Long userId) {
        return jobContractRepository.getTotalJobExpenditureByUserId(userId).orElse(0l);
    }

    public Long getTotalJobCompletedByUser(Long userId) {
        return jobContractRepository.getTotalJobCompletedByUser(userId).orElse(0l);
    }
    public JobContract findJobContractByProposalAndJobId(Long proposalId, Long jobId) {
        return jobContractRepository.findContractByProposalAndJobId(proposalId, jobId);
    }
    public JobContractExtension findContractExtensionByProposalId(Long proposalId, Long userId) {
        return jobContractExtensionRepository.findExtensionByProposalAndUserId(proposalId, userId);
    }
    public JobContractExtension findContractExtensionByContractReference(String contractReference) {
        return jobContractExtensionRepository.findExtensionByContractReference(contractReference);
    }
    public JobProjectSubmission findJobSubmittedByProposalId(Long proposalId, Long userId) {
        return jobProjectSubmissionRepository.findSubmittedProjectByProposalAndUserId(proposalId, userId);
    }
    public JobProjectSubmission findJobSubmittedByContractReference(String contractReference) {
        return jobProjectSubmissionRepository.findSubmittedProjectByContractReference(contractReference);
    }
    public JobMilestone findContractMilestoneByProposalId(Long proposalId, Long userId) {
        return jobMilestoneRepository.findMilestoneByProposalAndJobId(proposalId, userId);
    }

    public JobMilestone findMilestoneById(Long milestoneId) {
        return jobMilestoneRepository.findById(milestoneId).orElse(null);
    }

    public JobMilestone findContractMilestoneByJobId(Long proposalId, Long jobId) {
        return jobMilestoneRepository.findMilestoneByProposalAndJobId(proposalId, jobId);
    }

    public Long findTotalSpentAmountByContractReference(String  contractReference) {
        return jobMilestoneRepository.findTotalSpentAmountByProposalId(contractReference);
    }

    public List<JobMilestone> findAllContractMilestoneByProposalJobIdAndStatus(Long proposalId, Long jobId, String status) {
        return jobMilestoneRepository.findAllMilestonesByProposalJobAndStatus(proposalId, jobId, status);
    }

    public List<JobMilestone> findAllContractMilestoneByProposalJobId(Long proposalId, Long jobId) {
        return jobMilestoneRepository.findAllMilestonesByProposalAndJobId(proposalId, jobId);
    }

    public APIResponse approveJobProposal(String authToken, OAuth2Authentication authentication, String request) throws JsonProcessingException {

        JobContract contract = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).readValue(request, JobContract.class);
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        return approveJobProposal(authToken,currentUser, contract);
    }

    public APIResponse approveJobProposal(String authToken,JwtUserDetail currentUser, JobContract contract) {
        try {
            JobProposal proposal = jobProposalRepository.findById(contract.getProposalId()).orElse(null);
            Job job = jobRepository.findById(proposal.getJobId()).orElse(null);
            String contractReferenceId = app.makeUIID();
            String paymentReferenceId = app.makeUIID();
            contract.setPeppfees(0); //set peprest charges to zero
            contract.setContractReference(contractReferenceId);
            contract.setInitialPaymentReferenceA(paymentReferenceId);
            contract.setAppId(Integer.valueOf(appId));
            contract.setClearedAmount(0);

            int status = 0;
            String remark = null;

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, proposal.getDuration().intValue());
            contract.setEndDate(c.getTime());
            contract.setStartDate(new Date());
            contract.setStatus(Status.WP);
            proposal.setStatus(Status.WP);
            job.setStatus(Status.WP);
            proposal.setLastModifiedDate(new Date());
            proposal.setStartDate(new Date());
            proposal.setEndDate(c.getTime());

            if(contract.getPaymentMethod()==null)
                contract.setPaymentMethod(PaymentMethod.BANK);

            if (contract.getWorkMethod().equals("Milestone")) {
                status = 1;
                //fire notification
                User user =userService.getUserById(proposal.getUserId());
                if (job != null && user!=null) {
                    NotificationBody body = new NotificationBody();
                    body.setBody(currentUser.getUserFullName() + " approved your contract and the amount will be paid to you base on the milestone you complete");
                    body.setSubject("Proposal Approval");
                    body.setActionType("REDIRECT");
                    body.setAction("/job/ongoing/details/" + proposal.getJobId());
                    body.setTopic("'Job'");
                    body.setChannel("S");
                    body.setPriority("YES");
                    body.setRecipient(proposal.getUserId());
                    body.setRecipientEmail(user.getEmail());
                    body.setRecipientName(user.getFullName());
                    notificationSender.pushNotification(body);
                }
                //end
            } else {

                try {
                    //get configurations
                    List<Config> configs = configService.getConfigs();
                    app.print(configs);
                    if (!configs.isEmpty()) {
                        for (Config currentConfig : configs) {
                            //#getting escrow account
                            if (currentConfig.getReference().equals(ConfigReference.ESCROW_ACCOUNT_NAME))
                                this.escrowAccountName = currentConfig.getValue();
                            if (currentConfig.getReference().equals(ConfigReference.ESCROW_ACCOUNT_NUMBER))
                                this.escrowAccountNumber = currentConfig.getValue();
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    return new APIResponse("Unable to extract job service configurations", false, null);
                }

                app.print("###ESCROW ACCOUNT");
                app.print(this.escrowAccountName);
                app.print(this.escrowAccountNumber);

                app.print("Contract Details:");
                app.print(contract);

                Boolean isPaid=false;

                if(contract.getPaymentMethod().equals(PaymentMethod.WALLET)) {
                    //transfer the amount to Kula main account from Wallet
                    PaymentRequest payment = new PaymentRequest();
                    payment.setProposalId(contract.getProposalId());
                    payment.setAmount(contract.getAmount());
                    payment.setNarration("Transfer from Employer to Escrow account for job Contract");
                    payment.setCreditAccountName(escrowAccountName);
                    payment.setCreditAccountNumber(escrowAccountNumber);
                    payment.setCreditAccountType("GL");
                    payment.setPaymentReference(paymentReferenceId);
                    payment.setExecutedFor(contractReferenceId);
                    payment.setContractReference(contractReferenceId);
                    payment.setExecutedBy(currentUser.getUserUUID());
                    payment.setExecutedByUsername(currentUser.getUserEmail());

                    APIResponse paymentResponse = jobWalletPaymentService.debitWallet(currentUser, payment);
                    if (paymentResponse.isSuccess()) {
                        try {
                            isPaid=true;
                            //############### Activity Logging ###########
                            ActivityLog log = new ActivityLog();
                            log.setDescription("Employer payment to Escrow Successful for job "+job.getTitle());
                            log.setRequestObject(app.toString(payment));
                            log.setResponseObject(app.toString(paymentResponse));
                            log.setUsername(currentUser.getUserEmail());
                            log.setUserId(currentUser.getUserUUID());
                            appLogger.log(log);
                            //#########################################
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        contract.setInitialPaymentReferenceB(paymentResponse.getPayload().toString());
                    } else {
                        isPaid=false;
                    }

                }
                else {
                    //transfer the amount to Sidekiq main account
                    PaymentRequest payment = new PaymentRequest();
                    payment.setProposalId(contract.getProposalId());
                    payment.setAmount(contract.getAmount());
                    payment.setNarration("Transfer from Employer to Escrow account for job Contract");
                    payment.setCreditAccountName(escrowAccountName);
                    payment.setCreditAccountNumber(escrowAccountNumber);
                    payment.setCreditAccountType("GL");
                    payment.setDebitAccountName(contract.getEmployerAccountName());
                    payment.setDebitAccountNumber(contract.getEmployerAccountNumber());
                    payment.setDebitAccountType("CASA");
                    payment.setPaymentReference(paymentReferenceId);
                    payment.setExecutedFor(contractReferenceId);
                    payment.setContractReference(contractReferenceId);
                    payment.setExecutedBy(currentUser.getUserUUID());
                    payment.setExecutedByUsername(currentUser.getUserEmail());

                    APIResponse paymentResponse = jobPaymentService.makePayment(authToken,payment);
                    if (paymentResponse.isSuccess()) {
                        try {
                            isPaid = true;
                            //############### Activity Logging ###########
                            ActivityLog log = new ActivityLog();
                            log.setDescription("Employer payment to Escrow Successful for job " + job.getTitle());
                            log.setRequestObject(app.toString(payment));
                            log.setResponseObject(app.toString(paymentResponse));
                            log.setUsername(currentUser.getUserEmail());
                            log.setUserId(currentUser.getUserUUID());
                            appLogger.log(log);
                            //#########################################
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        contract.setInitialPaymentReferenceB(paymentResponse.getPayload().toString());
                    } else {
                        isPaid = false;
                    }
                }

                if(isPaid){

                   if (job != null) {
                        NotificationBody body = new NotificationBody();
                        body.setBody("Your payment of " + contract.getAmount() + " has been successful");
                        body.setSubject("Payment Successful");
                        body.setActionType("REDIRECT");
                        body.setAction("/job/ongoing/details/" + proposal.getJobId());
                        body.setTopic("'Job'");
                        body.setChannel("S");
                        body.setPriority("NORMAL");
                        body.setRecipient(proposal.getEmployerId());
                        body.setRecipientEmail(currentUser.getUserEmail());
                        body.setRecipientName(currentUser.getUserFullName());
                        notificationSender.pushNotification(body);
                    }

                    app.print("Creating Escrow Request###################################");
                    app.print("Escrow URL: "+baseURL);
                    app.print("Escrow Token: "+token);

                    JSONObject requestPayload = new JSONObject();
                    requestPayload.put("appid",appId);
                    requestPayload.put("referenceid",contract.getContractReference());
                    requestPayload.put("user_email",contract.getUserEmail());
                    requestPayload.put("amount",contract.getAmount());
                    requestPayload.put("country",contract.getCountry());
                    requestPayload.put("currency",contract.getCurrency());
                    requestPayload.put("customer_email",contract.getFreelancerEmailAddress());
                    requestPayload.put("merchant_email",contract.getEmployerEmailAddress());
                    requestPayload.put("customer_account_number",contract.getFreelancerAccountNumber());
                    requestPayload.put("merchant_account_number",contract.getEmployerAccountNumber());
                    requestPayload.put("customer_bank_code","032");
                    requestPayload.put("merchant_bank_code","032");
                    requestPayload.put("customer_name",contract.getFreelancerAccountName());
                    requestPayload.put("merchant_name",contract.getEmployerAccountName());
                    requestPayload.put("customer_phone",contract.getFreelancerPhoneNumber());
                    requestPayload.put("merchant_phone",contract.getEmployerPhoneNumber());
                    requestPayload.put("peppfees",contract.getPeppfees());
                    requestPayload.put("startdate",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(contract.getStartDate()));
                    requestPayload.put("enddate", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(contract.getEndDate()));
                    requestPayload.put("transfer_reference_id",contract.getContractReference() );

                    app.print("Request Body");
                    app.print(requestPayload);
                    String endpoint=baseURL + "/Transaction/create";
                    HttpEntity<String> requestEntity = new HttpEntity<>(requestPayload.toString(), this.getHeaders());
                    ResponseEntity<String> response = rest.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);
                    if (response.getStatusCode().is2xxSuccessful()) {
                        status = 1;
                        remark = "success";

                        app.print("Escrow response: "+response.getStatusCode().is2xxSuccessful());
                        //fire notifications
                        NotificationBody body1 = new NotificationBody();
                        String[] params = {String.valueOf(contract.getAmount()), job.getTitle()};
                        String message = messageSource.getMessage("proposal.approval.successful.email-body.gig-provider", params, LocaleContextHolder.getLocale());
                        body1.setBody(message);
                        body1.setSubject("Job Proposal Approved");
                        body1.setActionType("REDIRECT");
                        body1.setAction("/job/ongoing/details/" + proposal.getJobId());
                        body1.setTopic("'Job'");
                        body1.setChannel("S");
                        body1.setPriority("YES");
                        body1.setRecipient(proposal.getEmployerId());
                        body1.setRecipientEmail(currentUser.getUserEmail());
                        body1.setRecipientName(currentUser.getUserFullName());
                        notificationSender.pushNotification(body1);

                        User user =userService.getUserById(proposal.getUserId());
                        if(user!=null) {
                            NotificationBody body2 = new NotificationBody();
                            String[] params1 = {currentUser.getFirstName(), job.getTitle(), String.valueOf(contract.getAmount())};
                            String message1 = messageSource.getMessage("proposal.approval.successful.email-body.freelancer", params1, LocaleContextHolder.getLocale());
                            body2.setBody(message1);
                            body2.setSubject("Proposal Approval");
                            body2.setActionType("REDIRECT");
                            body2.setAction("/job/ongoing/details/" + proposal.getJobId());
                            body2.setTopic("'Job'");
                            body2.setChannel("S");
                            body2.setPriority("YES");
                            body2.setRecipient(proposal.getUserId());
                            body2.setRecipientEmail(user.getEmail());
                            body2.setRecipientName(user.getFullName());
                            notificationSender.pushNotification(body2);
                        }

                        //end
                    } else {
                        status = 0;
                        remark = "Escrow transaction failed";
                        logger.info("JOBSERVICE: Escrow transaction failed");
                        logger.error("JOBSERVICE: Escrow " + response.getBody());

                    }
                } else {
                    status = 0;
                    remark ="Payment Failed";
                }
            }
            if (status == 1) {
                if (job != null) {
                    job.setStatus(Status.WP);
                    job.setLastModifiedDate(new Date());
                    jobRepository.save(job);

                    if (job.getType() == JobType.TEAMS_PROJECT) {

                        JobTeamDetails existingMember = jobTeamDetailsRepository.findByProposalId(proposal.getId());
                        if (existingMember == null) {
                            // save approved team member
                            JobTeamDetails teamMember = new JobTeamDetails();
                            teamMember.setImg(proposal.getImg());
                            teamMember.setFullName(proposal.getFullName());
                            teamMember.setEmail(proposal.getEmail());
                            teamMember.setAmount(proposal.getBidAmount());
                            teamMember.setJobId(proposal.getJobId());
                            teamMember.setUserId(proposal.getUserId());
                            teamMember.setProposalId(proposal.getId());
                            teamMember.setEmployerId(proposal.getEmployerId());
                            teamMember.setStatus(Status.AC);
                            teamMember.setDescription(proposal.getAbout());
                            teamMember.setPercentage(Long.valueOf(10));
                            teamMember.setStartDate(new Date());
                            teamMember.setEndDate(c.getTime());
                            jobTeamDetailsRepository.save(teamMember);
                        } else {
                            existingMember.setStartDate(new Date());
                            existingMember.setEndDate(c.getTime());
                            existingMember.setStatus(Status.AC);
                            jobTeamDetailsRepository.save(existingMember);
                        }
                    }
                } else {
                    logger.info("JOBSERVICE: Job not found");
                    remark = "Job not found";
                }

                app.print(contract);
                JobContract savedContract = jobContractRepository.save(contract);
                proposal.setContractId(savedContract.getId());
                jobProposalRepository.save(proposal);
                jobRepository.save(job);

                try {
                    //############### Activity Logging ###########
                    ActivityLog log = new ActivityLog();
                    log.setDescription("Contract Approved Successfully for job "+job.getTitle());
                    log.setRequestObject(app.toString(contract));
                    log.setResponseObject(app.toString(savedContract));
                    log.setUsername(currentUser.getUserEmail());
                    log.setUserId(currentUser.getUserUUID());
                    appLogger.log(log);
                    //#########################################
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return new APIResponse(remark, true, savedContract);
            } else {
                logger.info("JOBSERVICE: Transaction failed");
                return new APIResponse(remark, false, null);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse("Oops! An Error Occurred", false, null);
        }
    }

    public JobContractExtension requestContractExtension(OAuth2Authentication authentication, String request) throws JsonProcessingException {

        JobContractExtension extensionRequest = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).readValue(request, JobContractExtension.class);
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        return requestContractExtension(currentUser, extensionRequest);

    }

    public JobContractExtension requestContractExtension(JwtUserDetail currentUser, JobContractExtension extensionRequest) {
        try {
            JobContract contract= jobContractRepository.findContractByProposalAndJobId(extensionRequest.getProposalId(), extensionRequest.getJobId());
            if(contract!=null) {
                extensionRequest.setStatus(Status.PA);
                extensionRequest.setContractReference(contract.getContractReference());
                JobContractExtension extension = jobContractExtensionRepository.save(extensionRequest);
                if (extension != null) {
                    //fire notification
                    Job currentJob = jobRepository.findById(extension.getJobId()).orElse(null);
                    User employer =userService.getUserById(extension.getEmployerId());
                    if (currentJob != null && employer!=null) {
                        NotificationBody body = new NotificationBody();
                        body.setBody(currentUser.getUserFullName() + " want you to help extend delivery date for " + currentJob.getTitle() + " to " + extension.getDate().toString());
                        body.setSubject("Contract Extension");
                        body.setActionType("REDIRECT");
                        body.setAction("/job/ongoing/details/" + extension.getJobId());
                        body.setTopic("'Job'");
                        body.setChannel("S");
                        body.setPriority("YES");
                        body.setRecipient(extension.getEmployerId());
                        body.setRecipientEmail(employer.getEmail());
                        body.setRecipientName(employer.getFullName());
                        notificationSender.pushNotification(body);
                    }
                    //end

                    try {
                        //############### Activity Logging ###########
                        ActivityLog log = new ActivityLog();
                        log.setDescription("Contract Timeline Extension Request for contract Id"+contract.getId());
                        log.setRequestObject(app.toString(extensionRequest));
                        log.setResponseObject(app.toString(extension));
                        log.setUsername(currentUser.getUserEmail());
                        log.setUserId(currentUser.getUserUUID());
                        appLogger.log(log);
                        //#########################################
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    return extension;
                } else {
                    logger.info("JOBSERVICE: Unable to submit the contract extension");
                    return null;
                }
            }else{
                logger.info("JOBSERVICE: Contract not found");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JobMilestone addNewMilestone(OAuth2Authentication authentication, String request) throws JsonProcessingException {
        JobMilestone milestone = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).readValue(request, JobMilestone.class);
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        return addNewMilestone(currentUser, milestone);
    }


    public JobMilestone addNewMilestone(JwtUserDetail currentUser, JobMilestone milestone) {
        try {
            JobContract contract=jobContractRepository.findById(milestone.getContractId()).orElse(null);
            if(contract!=null) {
                String milestoneReferenceId = app.makeUIID();
                milestone.setStatus(Status.PC);
                milestone.setContractReference(contract.getContractReference());
                milestone.setMilestoneReference(milestoneReferenceId);

                JobMilestone newMilestone = jobMilestoneRepository.save(milestone);
                if (newMilestone != null) {
                    //fire notification
                    Job currentJob = jobRepository.findById(newMilestone.getJobId()).orElse(null);
                    User employer =userService.getUserById(newMilestone.getEmployerId());
                    if (currentJob != null && employer!=null) {
                        NotificationBody body = new NotificationBody();
                        body.setBody(currentUser.getUserFullName() + " created new milestone on " + currentJob.getTitle() + " for your review and approval");
                        body.setSubject("New Milestone");
                        body.setActionType("REDIRECT");
                        body.setAction("/my-job/contract/milestones/" + newMilestone.getJobId() + "/" + newMilestone.getProposalId());
                        body.setTopic("'Job'");
                        body.setChannel("S");
                        body.setPriority("YES");
                        body.setRecipient(newMilestone.getEmployerId());
                        body.setRecipientEmail(employer.getEmail());
                        body.setRecipientName(employer.getFullName());
                        notificationSender.pushNotification(body);
                    }
                    //end

                    try {
                        //############### Activity Logging ###########
                        ActivityLog log = new ActivityLog();
                        log.setDescription("Created new Milestone for job "+currentJob.getTitle());
                        log.setRequestObject(app.toString(milestone));
                        log.setResponseObject(app.toString(newMilestone));
                        log.setUsername(currentUser.getUserEmail());
                        log.setUserId(currentUser.getUserUUID());
                        appLogger.log(log);
                        //#########################################
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    return newMilestone;
                } else {
                    logger.info("JOBSERVICE: Unable to add new milestone");
                    return null;
                }
            }else{
                logger.info("JOBSERVICE: Contract not found for the milestone");
                return  null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public JobContractExtension approveContractExtension(OAuth2Authentication authentication, String request) throws JsonProcessingException {
        JobContractExtension extensionRequest = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).readValue(request, JobContractExtension.class);
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        return approveContractExtension(currentUser, extensionRequest);
    }

    public JobContractExtension approveContractExtension(JwtUserDetail currentUser, JobContractExtension
            request) {
        try {
            JobContractExtension extension = jobContractExtensionRepository.findExtensionByProposalAndJobId(request.getProposalId(), request.getJobId());
            ResponseEntity<String> response = null;

            if (extension != null) {
                JobContract contract = jobContractRepository.findContractByProposalAndJobId(request.getProposalId(), request.getJobId());
                if (contract != null) {
                    //set end date from today
                    contract.setEndDate(extension.getDate());
                    contract.setLastModifiedDate(new Date());

                    app.print("###################################");
                    app.print("Escrow URL: "+baseURL);
                    app.print("Escrow Token: "+token);
                    //start to extend escrow live

                    JSONObject requestPayload = new JSONObject();
                    requestPayload.put("appid",appId);
                    requestPayload.put("referenceid",contract.getContractReference());
                    requestPayload.put("user_email",contract.getUserEmail());
                    requestPayload.put("new_date", extension.getDate().toString() );
                    requestPayload.put("action","accept");
                    requestPayload.put("reasons",extension.getReason() );

                    app.print("Request Body");
                    app.print(requestPayload);
                    String endpoint=baseURL + "/Transaction/reqExtension";
                    HttpEntity<String> requestEntity = new HttpEntity<>(requestPayload.toString(), this.getHeaders());
                    response = rest.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);
                    //done
                    if (response.getStatusCode().is2xxSuccessful()) {
                        app.print("Escrow response: "+response.getStatusCode().is2xxSuccessful());
                        jobContractRepository.save(contract);
                    }
                    else
                        logger.info("JOBSERVICE: Escrow transaction failed");
                }

                Optional<JobProposal> proposalData = jobProposalRepository.findById(extension.getProposalId());
                JobProposal proposal = proposalData.orElse(null);
                if (proposal != null) {
                    proposal.setLastModifiedDate(new Date());
                    proposal.setDuration((decodeDuration(proposal.getDuration(), proposal.getDurationType()) + getDifferenceDays(proposal.getEndDate(), extension.getDate())));
                    proposal.setDurationType("D");
                    proposal.setEndDate(extension.getDate());
                }

                if (response != null) {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        extension.setStatus(Status.AC);
                        extension.setLastModifiedBy(currentUser.getUserEmail());
                        extension.setLastModifiedDate(new Date());
                        extension.setApprovedDate(new Date());
                        jobContractExtensionRepository.save(extension);

                        //fire notification
                        Job currentJob = jobRepository.findById(extension.getJobId()).orElse(null);
                        User freelancer=userService.getUserById(extension.getUserId());
                        if (proposal != null && currentJob != null && freelancer!=null) {
                            NotificationBody body = new NotificationBody();
                            body.setBody(currentUser.getUserFullName() + "  approved  your request for the delivery date extension as requested");
                            body.setSubject("Contract Extension Approved");
                            body.setActionType("REDIRECT");
                            body.setAction("/job/ongoing/details/" + extension.getJobId());
                            body.setTopic("'Job'");
                            body.setChannel("S");
                            body.setPriority("YES");
                            body.setRecipient(extension.getUserId());
                            body.setRecipientEmail(freelancer.getEmail());
                            body.setRecipientName(freelancer.getFullName());
                            notificationSender.pushNotification(body);
                        }
                        //end

                        try {
                            //############### Activity Logging ###########
                            ActivityLog log = new ActivityLog();
                            log.setDescription("Approved Contract Extension for Contract "+contract.getId());
                            log.setRequestObject(app.toString(request));
                            log.setResponseObject(app.toString(extension));
                            log.setUsername(currentUser.getUserEmail());
                            log.setUserId(currentUser.getUserUUID());
                            appLogger.log(log);
                            //#########################################
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }

                        return extension;
                    } else {
                        logger.info("JOBSERVICE: Escrow transaction failed");
                        return null;
                    }
                } else {
                    logger.info("JOBSERVICE: Escrow transaction failed");
                    return null;
                }

            } else {
                logger.info("JOBSERVICE: Extension request not found");
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public JobProjectSubmission submitJob(OAuth2Authentication authentication, String
            projectData, MultipartFile[] supportingFiles) throws JsonProcessingException {

        JobProjectSubmission request = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).readValue(projectData, JobProjectSubmission.class);
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        return submitJob(currentUser, request, supportingFiles);

    }

    public JobProjectSubmission submitJob(JwtUserDetail currentUser, JobProjectSubmission
            request, MultipartFile[] supportingFiles) {
        try {

            app.print("Submitting job>>>>");
            app.print(request);
            String supporting_file_names = null;
            request.setStatus(Status.PE);

            if (supportingFiles != null)
                supporting_file_names = this.fileStoreService.storeFiles(supportingFiles, request.getProposalId().toString());

            if (supporting_file_names != null)
                request.setSupportingFiles(supporting_file_names);

            //fire notification
            app.print("It is here");
            Job currentJob = jobRepository.findById(request.getJobId()).orElse(null);
            User employer=userService.getUserById(request.getEmployerId());
            if (currentJob != null && employer!=null) {
                app.print("Its here 2");
                NotificationBody body = new NotificationBody();
                String[] params = {currentJob.getTitle(), currentUser.getUserFullName()};
                String message = messageSource.getMessage("submit-job.email-body", params, LocaleContextHolder.getLocale());
                body.setBody(message);
                body.setSubject("Project Review");
                body.setActionType("REDIRECT");
                body.setAction("/job/ongoing/details/" + request.getJobId());
                body.setTopic("'Job'");
                body.setChannel("S");
                body.setPriority("YES");
                body.setRecipient(request.getEmployerId());
                body.setRecipientEmail(employer.getEmail());
                body.setRecipientName(employer.getFullName());
                notificationSender.pushNotification(body);
            }
            //end


            app.print("It is here 3");
           JobProjectSubmission savedRequest=  jobProjectSubmissionRepository.save(request);
            try {
                //############### Activity Logging ###########
                ActivityLog log = new ActivityLog();
                log.setDescription("Submitted Completed Project for Employer Review");
                log.setRequestObject(app.toString(request));
                log.setResponseObject(app.toString(savedRequest));
                log.setUsername(currentUser.getUserEmail());
                log.setUserId(currentUser.getUserUUID());
                appLogger.log(log);
                //#########################################
            }catch (Exception ex){
                ex.printStackTrace();
            }

            app.print("Done");
            return savedRequest;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public APIResponse raiseDispute(OAuth2Authentication authentication, String
            projectData, MultipartFile[] attachmentFiles) throws
            JsonProcessingException {
        JobContractDispute request = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).readValue(projectData, JobContractDispute.class);
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        return raiseDispute(currentUser, currentUser.getUserId(), request, attachmentFiles);
    }

    public APIResponse raiseDispute(JwtUserDetail currentUser, Long
            userId, JobContractDispute request, MultipartFile[] attachmentFiles) {
        try {
            String attachments = null;
            ResponseEntity<String> response = null;
            request.setStatus(Status.AC);
            request.setUserId(userId);

            app.print(request);

            if (attachmentFiles != null)
                attachments = this.fileStoreService.storeFiles(attachmentFiles, request.getProposalId().toString());
            if (attachments != null)
                request.setAttachment(attachments);

            app.print("###################################");
            app.print("Escrow URL: "+baseURL);
            app.print("Escrow Token: "+token);

            JSONObject requestPayload = new JSONObject();
            requestPayload.put("appid",appId);
            requestPayload.put("referenceid",request.getReferenceId());
            requestPayload.put("dispute_referenceid",request.getContractReference());
            requestPayload.put("dispute_category", "contract-" + request.getContractId().toString());
            requestPayload.put("dispute_description",request.getDescription());

            app.print("Request Body");
            app.print(requestPayload);
            String endpoint=baseURL + "/Dispute/reportDispute";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestPayload.toString(), this.getHeaders());
            response = rest.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);
            //done
            if (response.getStatusCode().is2xxSuccessful()) {
                //fire notification
                JobContractDispute dispute = jobContractDisputeRepository.save(request);
                User employer=userService.getUserById(dispute.getEmployerId());

                if (dispute != null && employer!=null) {
                    Job currentJob = jobRepository.findById(dispute.getJobId()).orElse(null);
                    if (currentJob != null) {
                        NotificationBody body = new NotificationBody();
                        body.setBody(currentUser.getUserFullName() + " raised a dispute on " + currentJob.getTitle() + "");
                        body.setSubject("Dispute Raised Against you");
                        body.setActionType("REDIRECT");
                        body.setAction("/job/details/" + dispute.getJobId());
                        body.setTopic("'Job'");
                        body.setChannel("S");
                        body.setPriority("YES");
                        body.setRecipient(dispute.getEmployerId());
                        body.setRecipientEmail(employer.getEmail());
                        body.setRecipientName(employer.getFullName());
                        notificationSender.pushNotification(body);
                    }


                    try {
                        //############### Activity Logging ###########
                        ActivityLog log = new ActivityLog();
                        log.setDescription("Raised Dispute for  "+currentJob.getTitle());
                        log.setRequestObject(app.toString(request));
                        log.setResponseObject(app.toString(dispute));
                        log.setUsername(currentUser.getUserEmail());
                        log.setUserId(currentUser.getUserUUID());
                        appLogger.log(log);
                        //#########################################
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                    return new APIResponse("success", true, dispute);

                } else {
                    logger.info("JOBSERV: Unable to raise a dispute, the dispute request is not valid");
                    return new APIResponse("Unable to raise a dispute, the dispute request is not valid", false, null);
                }
            } else {
                logger.info("JOBSERVICE: Escrow transaction failed");
                return new APIResponse("Escrow transaction failed", false, null);
            }
            //end

        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse(ex.getMessage(), false, null);
        }
    }

    public JobProjectSubmission rejectJob(OAuth2Authentication authentication, RejectionRequest rejectionRequest, Long jobId, Long
            requestId) {

        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        JobProjectSubmission request = jobProjectSubmissionRepository.findById(requestId).orElse(null);
        if (request != null) {
            request.setStatus(Status.RE);
            request.setRemark(rejectionRequest.getReason());


            User freelancer=userService.getUserById(request.getUserId());
            if(freelancer!=null) {
                //fire notification
                NotificationBody body = new NotificationBody();
                body.setBody("Job that you submitted has been rejected by the employer");
                body.setSubject("Project Rejected");
                body.setActionType("REDIRECT");
                body.setAction("/job/ongoing/details/" + request.getJobId());
                body.setTopic("'Job'");
                body.setChannel("S");
                body.setPriority("YES");
                body.setRecipient(request.getUserId());
                body.setRecipientEmail(freelancer.getEmail());
                body.setRecipientName(freelancer.getFullName());
                notificationSender.pushNotification(body);
            }
        }

        JobProjectSubmission savedRequest= jobProjectSubmissionRepository.save(request);
        try {
            //############### Activity Logging ###########
            ActivityLog log = new ActivityLog();
            log.setDescription("Rejected a Project Submitted");
            log.setRequestObject(app.toString(request));
            log.setResponseObject(app.toString(savedRequest));
            log.setUsername(currentUser.getUserEmail());
            log.setUserId(currentUser.getUserUUID());
            appLogger.log(log);
            //#########################################
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return request;
    }

    public JobProjectSubmission submitCompletedMilestone(OAuth2Authentication authentication, Long milestoneId, String projectData, MultipartFile[] supportingFiles) throws
            JsonProcessingException {
        JobProjectSubmission request = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).readValue(projectData, JobProjectSubmission.class);
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        return submitCompletedMilestone(currentUser, milestoneId, request, supportingFiles);
    }


    public JobProjectSubmission submitCompletedMilestone(JwtUserDetail currentUser, Long milestoneId, JobProjectSubmission
                                                                 request, MultipartFile[] supportingFiles) {
        try {
            String supporting_file_names = null;
            JobMilestone milestone = jobMilestoneRepository.findById(milestoneId).orElse(null);
            if (supportingFiles != null)
                supporting_file_names = this.fileStoreService.storeFiles(supportingFiles, request.getProposalId().toString());

            if (supporting_file_names != null) {
                request.setSupportingFiles( supporting_file_names);
                milestone.setSupportingFiles(supporting_file_names);
            }

            if (milestone != null) {
                milestone.setStatus(Status.PA);
                jobMilestoneRepository.save(milestone);

                //fire notification
                Job currentJob = jobRepository.findById(request.getJobId()).orElse(null);
                User employer = userService.getUserById(request.getEmployerId());
                if (currentJob != null && employer!=null) {
                    NotificationBody body = new NotificationBody();
                    body.setBody(currentUser.getUserFullName() + " submitted milestone for your review and approval");
                    body.setSubject("Milestone Review");
                    body.setActionType("REDIRECT");
                    body.setAction("/my-job/contract/milestones/" + request.getJobId() + "/" + request.getProposalId());
                    body.setTopic("'Job'");
                    body.setChannel("S");
                    body.setPriority("YES");
                    body.setRecipient(request.getEmployerId());
                    body.setRecipientEmail(employer.getEmail());
                    body.setRecipientName(employer.getFullName());
                    notificationSender.pushNotification(body);
                }
                //end
                request.setStatus(Status.PA);
                JobProjectSubmission savedRequest= jobProjectSubmissionRepository.save(request);

                try {
                    //############### Activity Logging ###########
                    ActivityLog log = new ActivityLog();
                    log.setDescription("Submitted new Milestone Job for Employer Review");
                    log.setRequestObject(app.toString(request));
                    log.setResponseObject(app.toString(savedRequest));
                    log.setUsername(currentUser.getUserEmail());
                    log.setUserId(currentUser.getUserUUID());
                    appLogger.log(log);
                    //#########################################
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                return  savedRequest;
            } else {
                logger.info("JOBSERVICE: Milestone not found");
                return null;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public APIResponse endContract(String authToken, OAuth2Authentication authentication, Rate
            rating, Long jobId, Long proposalId, Long userId, int state) {
        try {
            JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
            Job job = jobRepository.findById(jobId).orElse(null);
            JobProposal proposal = jobProposalRepository.findProposalByUserId(jobId, userId);
            JobContract contract = jobContractRepository.findContractByProposalAndJobId(proposalId, jobId);
            JobProjectSubmission project = jobProjectSubmissionRepository.findSubmittedProjectByProposalAndUserId(proposalId, userId);

            if (contract != null) {
                contract.setRate(rating.getRate());
                contract.setDescription(rating.getDescription());
                contract.setFeedback(rating.getFeedback());


                if (state == 1) {
                    if (contract.getWorkMethod().equals("Overall")) {
                        if (contract.getStatus().equals(Status.WP)) {
                            contract.setEndDate(new Date());
                            contract.setIsSettled(true);
                            contract.setSettlement(contract.getContractReference());

                            app.print("###################################");
                            app.print("Escrow URL: "+baseURL);
                            app.print("Escrow Token: "+token);
                            //start to release escrow amount to freelancer

                            JSONObject requestPayload = new JSONObject();
                            requestPayload.put("appid",appId);
                            requestPayload.put("referenceid",contract.getContractReference());
                            requestPayload.put("user_email",contract.getUserEmail());
                            requestPayload.put("reasons", "Job Completed");

                            app.print("Request Body");
                            app.print(requestPayload);
                            String endpoint=baseURL + "/Transaction/release";
                            HttpEntity<String> requestEntity = new HttpEntity<>(requestPayload.toString(), this.getHeaders());
                            ResponseEntity<String> response = rest.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);
                            //done

                            if (response.getStatusCode().is2xxSuccessful()) {
                                APIResponse settlementResponse = this.settleContractById(authToken,authentication, contract.getContractReference());
                                if (settlementResponse.isSuccess()) {

                                    try {
                                        //############### Activity Logging ###########
                                        ActivityLog log = new ActivityLog();
                                        log.setDescription("Payment to Freelancer Successful for Contract"+contract.getId());
                                        log.setRequestObject(app.toString(contract));
                                        log.setResponseObject(app.toString(settlementResponse));
                                        log.setUsername(currentUser.getUserEmail());
                                        log.setUserId(currentUser.getUserUUID());
                                        appLogger.log(log);
                                        //#########################################
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                    //complete contract
                                    contract.setStatus(Status.CO);
                                    contract.setEndDate(new Date());
                                    jobContractRepository.save(contract);

                                    if (job != null) {
                                        job.setStatus(Status.CO);
                                        jobRepository.save(job);
                                    }
                                    if (proposal != null) {
                                        proposal.setStatus(Status.CO);
                                        proposal.setEndDate(new Date());
                                        jobProposalRepository.save(proposal);
                                    }
                                    if (project != null) {
                                        project.setStatus(Status.CO);
                                        jobProjectSubmissionRepository.save(project);
                                    }

                                    if (rating != null) {
                                        Rate rate = jobRateRepository.findByUserId(proposal.getUserId());
                                        if (rate != null) {
                                            rate.setRate((rate.getRate() + rating.getRate()));
                                            rate.setFeedback(rating.getFeedback());
                                            rate.setDescription(rating.getDescription());
                                            rate.setLastModifiedDate(new Date());
                                            jobRateRepository.save(rate);
                                        } else {
                                            rating.setLastModifiedDate(new Date());
                                            rating.setUserId(proposal.getUserId());
                                            jobRateRepository.save(rating);
                                        }
                                    }
                                    //fire notification
                                    Job currentJob = jobRepository.findById(jobId).orElse(null);
                                    User freelancer =userService.getUserById(proposal.getUserId());
                                    if (currentJob != null && freelancer!=null) {
                                        NotificationBody body = new NotificationBody();
                                        body.setBody(currentUser.getUserFullName() + " ended your contract and release the sum of " + proposal.getBidAmount() + " to your bank account");
                                        body.setSubject("Contract Ended");
                                        body.setActionType("REDIRECT");
                                        body.setAction("/job/ongoing/details/" + jobId);
                                        body.setTopic("'Job'");
                                        body.setChannel("S");
                                        body.setPriority("YES");
                                        body.setRecipient(proposal.getUserId());
                                        body.setRecipientEmail(freelancer.getEmail());
                                        body.setRecipientName(freelancer.getFullName());
                                        notificationSender.pushNotification(body);
                                    }

                                    try {
                                        //############### Activity Logging ###########
                                        ActivityLog log = new ActivityLog();
                                        log.setDescription("Ended Contract for job "+job.getTitle());
                                        log.setRequestObject("ContractId"+contract.getId());
                                        log.setResponseObject(app.toString(settlementResponse));
                                        log.setUsername(currentUser.getUserEmail());
                                        log.setUserId(currentUser.getUserUUID());
                                        appLogger.log(log);
                                        //#########################################
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
                                return settlementResponse;
                                //end
                            } else {
                                logger.info("JOBSERV: Escrow transaction Failed");
                                return new APIResponse("Escrow Transaction Failed", false, null);
                            }
                        } else {
                            logger.info("JOBSERV: Unable to end inActive contract");
                            return new APIResponse("Unable to end inActive contract", false, null);
                        }
                    } else {
                        //check if not ongoing milestone and complete the contract
                        List<JobMilestone> activeMilestones = jobMilestoneRepository.findOngoingMilestonesByContractReference(contract.getContractReference());
                        if (!activeMilestones.isEmpty()) {
                            logger.info("JOBSERV: Cant end ongoing contract");
                            return new APIResponse("Unable to end ongoing contract, end ongoing milestones first", false, null);
                        } else {
                            //end contract
                            contract.setStatus(Status.CO);
                            contract.setEndDate(new Date());
                            jobContractRepository.save(contract);

                            if (job != null) {
                                job.setStatus(Status.CO);
                                jobRepository.save(job);
                            }
                            if (proposal != null) {
                                proposal.setStatus(Status.CO);
                                proposal.setEndDate(new Date());
                                jobProposalRepository.save(proposal);
                            }
                            if (project != null) {
                                project.setStatus(Status.CO);
                                project.setLastModifiedDate(new Date());
                                project.setApprovedDate(new Date());
                                project.setLastModifiedBy(currentUser.getUserEmail());
                                jobProjectSubmissionRepository.save(project);
                            }
                            return new APIResponse("Request Successful", true, contract);
                        }
                    }
                } else {

                    if (contract.getWorkMethod().equals("Overall")) {
                        if (!contract.getStatus().equals(Status.CO) && !contract.getStatus().equals(Status.WP)) {
                            contract.setEndDate(new Date());
                            contract.setIsSettled(false);
                            contract.setFeedback(String.valueOf(state));
                            contract.setSettlement("Contract ended without settlement");
                            contract.setDescription("Employer ended the contract");
                            contract.setStatus(Status.RE);
                            if (project != null) {
                                project.setStatus(Status.RE);
                                jobProjectSubmissionRepository.save(project);
                            }
                            if (proposal != null) {
                                proposal.setStatus(Status.RE);
                                proposal.setEndDate(new Date());
                                jobProposalRepository.save(proposal);
                            }
                            jobContractRepository.save(contract);
                            //fire notification
                            Job currentJob = jobRepository.findById(jobId).orElse(null);
                            User freelancer =userService.getUserById(proposal.getUserId());
                            if (currentJob != null && freelancer!=null) {
                                NotificationBody body = new NotificationBody();
                                body.setBody(currentUser.getUserFullName() + " ended your contract for " + currentJob.getTitle() + ", you are not find with it? you can raise dispute.");
                                body.setSubject("Contract Ended");
                                body.setActionType("REDIRECT");
                                body.setAction("/job/details/" + jobId);
                                body.setTopic("'Job'");
                                body.setChannel("S");
                                body.setRecipient(proposal.getUserId());
                                body.setRecipientEmail(freelancer.getEmail());
                                body.setRecipientName(freelancer.getFullName());
                                notificationSender.pushNotification(body);
                            }
                            //end
                            return new APIResponse("Request Successful", true, contract);
                        } else {
                            return new APIResponse("Unable end ongoing Contract with settlement", false, null);
                        }
                    } else {
                        //check if not ongoing milestone and complete the contract
                        List<JobMilestone> activeMilestones = jobMilestoneRepository.findOngoingMilestonesByContractReference(contract.getContractReference());
                        if (!activeMilestones.isEmpty()) {
                            logger.info("JOBSERV: Cant reject contract that has ongoing milestones");
                            return new APIResponse("Unable to end contract that has unsettled milestones", false, null);
                        } else {
                            //end contract
                            contract.setStatus(Status.CO);
                            contract.setEndDate(new Date());
                            jobContractRepository.save(contract);

                            if (job != null) {
                                job.setStatus(Status.CO);
                                jobRepository.save(job);
                            }
                            if (proposal != null) {
                                proposal.setStatus(Status.CO);
                                proposal.setEndDate(new Date());
                                jobProposalRepository.save(proposal);
                            }
                            if (project != null) {
                                project.setStatus(Status.CO);
                                project.setLastModifiedDate(new Date());
                                project.setLastModifiedBy(currentUser.getUserEmail());
                                jobProjectSubmissionRepository.save(project);
                            }
                            return new APIResponse("Request Successful", true, contract);
                        }
                    }
                }
            }
            else {
                return new APIResponse("Contract not found", false, null);
            }
        } catch (Exception ex) {
            logger.info(ex.getMessage());
            ex.printStackTrace();
            return new APIResponse(ex.getMessage(), false, null);
        }
    }


    public APIResponse modifyMilestoneState(String authToken,OAuth2Authentication authentication, Long milestoneId, String newStatus) {

        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        return modifyMilestoneState(authToken,currentUser, milestoneId, Status.valueOf(newStatus));

    }

    public APIResponse modifyMilestoneState(String authToken, JwtUserDetail
                                                    currentUser, Long milestoneId, Status
                                                    newStatus) {

        try {
            JobMilestone milestone = jobMilestoneRepository.findById(milestoneId).orElse(null);
            int status;
            String remark;

            if (milestone != null) {

                if (newStatus.equals(Status.AC)) {
                    milestone.setStatus(Status.AC);
                    milestone.setStartDate(new Date());
                    JobProposal proposal = jobProposalRepository.findById(milestone.getProposalId()).orElse(null);
                    Job job = jobRepository.findById(proposal.getJobId()).orElse(null);
                    if (proposal != null) {
                        proposal.setStatus(Status.WP);
                        //send milestone amount to escrow
                        JobContract contract = jobContractRepository.findById(proposal.getContractId()).orElse(null);
                        if (contract != null) {

                            String paymentReferenceId = app.makeUIID();
                            milestone.setInitialPaymentReferenceA(paymentReferenceId);
                            milestone.setStatus(Status.WP);
                            milestone.setContractId(contract.getId());
                            milestone.setContractReference(contract.getContractReference());

                            contract.setStatus(Status.WP);
                            contract.setLastModifiedDate(new Date());
                            contract.setInitialPaymentReferenceA(paymentReferenceId);
                            contract.setLastModifiedDate(new Date());
                                                        Calendar c = Calendar.getInstance();
                            c.setTime(new Date());
                            c.add(Calendar.DATE, proposal.getDuration().intValue());
                            milestone.setEndDate(c.getTime());
                            milestone.setStartDate(new Date());

                            try {
                                //get configurations
                                List<Config> configs = configService.getConfigs();
                                app.print(configs);
                                if (!configs.isEmpty()) {
                                    for (Config currentConfig : configs) {
                                        //#getting escrow account
                                        if (currentConfig.getReference().equals(ConfigReference.ESCROW_ACCOUNT_NAME))
                                            this.escrowAccountName = currentConfig.getValue();
                                        if (currentConfig.getReference().equals(ConfigReference.ESCROW_ACCOUNT_NUMBER))
                                            this.escrowAccountNumber = currentConfig.getValue();
                                    }
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                return new APIResponse("Unable to extract job service configurations", false, null);
                            }

                            app.print("###ESCROW ACCOUNT");
                            app.print(this.escrowAccountName);
                            app.print(this.escrowAccountNumber);


                            PaymentRequest payment = new PaymentRequest();
                            payment.setProposalId(contract.getProposalId());
                            payment.setAmount(milestone.getAmount());
                            payment.setNarration("Transfer from Employer to Escrow account for Milestone " + milestone.getTitle());
                            payment.setCreditAccountName(escrowAccountName);
                            payment.setCreditAccountNumber(escrowAccountNumber);
                            payment.setCreditAccountType("GL");
                            payment.setDebitAccountName(contract.getEmployerAccountName());
                            payment.setDebitAccountNumber(contract.getEmployerAccountNumber());
                            payment.setDebitAccountType("CASA");
                            payment.setPaymentReference(paymentReferenceId);
                            payment.setExecutedFor(contract.getContractReference());
                            payment.setContractReference(contract.getContractReference());
                            payment.setExecutedBy(currentUser.getUserUUID());
                            payment.setExecutedByUsername(currentUser.getUserEmail());

                            APIResponse paymentResponse=jobPaymentService.makePayment(authToken,payment);
                            if (paymentResponse.isSuccess()) {

                                milestone.setInitialPaymentReferenceB(paymentResponse.getPayload().toString());
                                contract.setInitialPaymentReferenceB(paymentResponse.getPayload().toString());

                                app.print("###################################");
                                app.print("Escrow URL: "+baseURL);
                                app.print("Escrow Token: "+token);

                                JSONObject requestPayload = new JSONObject();
                                requestPayload.put("appid",appId);
                                requestPayload.put("referenceid",milestone.getMilestoneReference());
                                requestPayload.put("user_email",contract.getUserEmail());
                                requestPayload.put("amount",milestone.getAmount());
                                requestPayload.put("country",contract.getCountry());
                                requestPayload.put("currency",contract.getCurrency());
                                requestPayload.put("customer_email",contract.getFreelancerEmailAddress());
                                requestPayload.put("merchant_email",contract.getEmployerEmailAddress());
                                requestPayload.put("customer_account_number",contract.getFreelancerAccountNumber());
                                requestPayload.put("merchant_account_number",contract.getEmployerAccountNumber());
                                requestPayload.put("customer_bank_code","032");
                                requestPayload.put("merchant_bank_code","032");
                                requestPayload.put("customer_name",contract.getFreelancerAccountName());
                                requestPayload.put("merchant_name",contract.getEmployerAccountName());
                                requestPayload.put("customer_phone",contract.getFreelancerPhoneNumber());
                                requestPayload.put("merchant_phone",contract.getEmployerPhoneNumber());
                                requestPayload.put("peppfees",contract.getPeppfees());
                                requestPayload.put("startdate",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(contract.getStartDate()));
                                requestPayload.put("enddate", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(contract.getEndDate()));
                                requestPayload.put("transfer_reference_id",milestone.getMilestoneReference() );

                                app.print("Request Body");
                                app.print(requestPayload);
                                String endpoint=baseURL + "/Transaction/create";
                                HttpEntity<String> requestEntity = new HttpEntity<>(requestPayload.toString(), this.getHeaders());
                                ResponseEntity<String> response = rest.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);

                                if (response.getStatusCode().is2xxSuccessful()) {
                                    jobRepository.save(job);
                                    jobProposalRepository.save(proposal);
                                    jobContractRepository.save(contract);
                                    status = 1;
                                    remark = "Escrow transaction succeeded";
                                    logger.info("JOBSERVICE: Escrow transaction succeeded");
                                } else {
                                    status = 0;
                                    remark = "Escrow transaction failed";
                                    logger.info("JOBSERVICE: Escrow transaction failed");
                                }

                                try {
                                    //############### Activity Logging ###########
                                    ActivityLog log = new ActivityLog();
                                    log.setDescription("Payment to Escrow Successful for Milestone of job "+job.getTitle());
                                    log.setRequestObject(app.toString(payment));
                                    log.setResponseObject(app.toString(paymentResponse));
                                    log.setUsername(currentUser.getUserEmail());
                                    log.setUserId(currentUser.getUserUUID());
                                    appLogger.log(log);
                                    //#########################################
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }

                                //done
                            } else {
                                status = 0;
                                remark = paymentResponse.getMessage();
                            }
                        } else {
                            status = 0;
                            remark = "Contract not found";
                            logger.error("JOBSERVICE: Contract not found");
                        }
                    } else {
                        status = 0;
                        remark = "Proposal not found";
                        logger.error("JOBSERVICE: Proposal not found");
                    }
                }
                else if(newStatus.equals(Status.RE)) {

                    milestone.setStatus(Status.RE);
                    milestone.setEndDate(new Date());
                    JobMilestone savedMilestone = jobMilestoneRepository.save(milestone);

                    if(savedMilestone!=null){

                        JobProjectSubmission project=jobProjectSubmissionRepository.findSubmittedProjectByContractReference(milestone.getMilestoneReference());
                        if(project!=null){
                            project.setStatus(Status.RE);
                            jobProjectSubmissionRepository.save(project);
                        }
                    }
                    //fire notification
                    Job currentJob = jobRepository.findById(milestone.getJobId()).orElse(null);
                    User freelancer=userService.getUserById(milestone.getUserId());
                    if (currentJob != null && freelancer!=null) {
                        NotificationBody body = new NotificationBody();
                        body.setBody(currentUser.getUserFullName() + " rejected the milestone you submitted for " + currentJob.getTitle() + ".");
                        body.setSubject("Milestone Rejected");
                        body.setActionType("REDIRECT");
                        body.setAction("/my-job/contract/milestones/" + milestone.getJobId() + "/" + milestone.getProposalId());
                        body.setTopic("'Job'");
                        body.setChannel("S");
                        body.setRecipient(milestone.getUserId());
                        body.setRecipientEmail(freelancer.getEmail());
                        body.setRecipientName(freelancer.getFullName());
                        notificationSender.pushNotification(body);
                    }

                    try {
                        //############### Activity Logging ###########
                        ActivityLog log = new ActivityLog();
                        log.setDescription("Rejected Milestone for job "+currentJob.getTitle());
                        log.setRequestObject(app.toString(milestone));
                        log.setResponseObject(app.toString(savedMilestone));
                        log.setUsername(currentUser.getUserEmail());
                        log.setUserId(currentUser.getUserUUID());
                        appLogger.log(log);
                        //#########################################
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                    //end
                    return new APIResponse("success", true, savedMilestone);
                }
                else {
                    app.print(milestone);
                    status = 0;
                    remark = "Milestone status mismatch";
                    logger.error("JOBSERVICE: Milestone status mismatch");
                }

                if (status == 1) {
                    //fire notification
                    Job currentJob = jobRepository.findById(milestone.getJobId()).orElse(null);
                    User freelancer=userService.getUserById(milestone.getUserId());

                    if (currentJob != null && freelancer!=null) {
                        NotificationBody body = new NotificationBody();
                        body.setBody(currentUser.getUserFullName() + " approved the milestone you submitted for " + currentJob.getTitle() + ", you can proceed to start working on it");
                        body.setSubject("Milestone Approval");
                        body.setActionType("REDIRECT");
                        body.setAction("/my-job/contract/milestones/" + milestone.getJobId() + "/" + milestone.getProposalId());
                        body.setTopic("'Job'");
                        body.setChannel("S");
                        body.setRecipient(milestone.getUserId());
                        body.setRecipientEmail(freelancer.getEmail());
                        body.setRecipientName(freelancer.getFullName());
                        notificationSender.pushNotification(body);
                    }


                    JobMilestone savedMilestone = jobMilestoneRepository.save(milestone);
                    try {
                        //############### Activity Logging ###########
                        ActivityLog log = new ActivityLog();
                        log.setDescription("Approved Milestone for job "+currentJob.getTitle());
                        log.setRequestObject(app.toString(milestone));
                        log.setResponseObject(app.toString(savedMilestone));
                        log.setUsername(currentUser.getUserEmail());
                        log.setUserId(currentUser.getUserUUID());
                        appLogger.log(log);
                        //#########################################
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    //end
                         return new APIResponse("success", true, savedMilestone);
                } else {
                    logger.info("JOBSERVICE: Transaction failed");
                    return new APIResponse(remark, false, null);
                }
            } else {
                logger.info("JOBSERVICE: Milestone request not found");
                remark = "Milestone request not found";
                return new APIResponse(remark, false, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse(ex.getMessage(), false, null);
        }
    }

    public APIResponse approveCompletedMilestone
            (String authToken, OAuth2Authentication authentication, String milestoneReference){
        try {
            JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
            JobMilestone milestone = jobMilestoneRepository.findMilestoneByMilestoneReference(milestoneReference).orElse(null);
            JobContract contract = jobContractRepository.findByContractReference(milestone.getContractReference()).orElse(null);
            JobProjectSubmission project = jobProjectSubmissionRepository.findSubmittedProjectByContractReference(milestone.getMilestoneReference());

            if(contract!=null) {
                if (milestone != null) {

                    if (project != null) {
                        contract.setLastModifiedDate(new Date());

                        app.print("###################################");
                        app.print("Escrow URL: "+baseURL);
                        app.print("Escrow Token: "+token);


                        JSONObject requestPayload = new JSONObject();
                        requestPayload.put("appid",appId);
                        requestPayload.put("referenceid",milestone.getMilestoneReference());
                        requestPayload.put("user_email",contract.getUserEmail());
                        requestPayload.put("reasons", "Milestone Completed");

                        app.print("Request Body");
                        app.print(requestPayload);
                        String endpoint=baseURL + "/Transaction/release";
                        HttpEntity<String> requestEntity = new HttpEntity<>(requestPayload.toString(), this.getHeaders());
                        ResponseEntity<String> response = rest.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);
                        //done
                        if (response.getStatusCode().is2xxSuccessful()) {

                            APIResponse settlementResponse=this.settleContractById(authToken,authentication,milestone.getMilestoneReference());
                            if(settlementResponse.isSuccess()) {

                                try {
                                    //############### Activity Logging ###########
                                    ActivityLog log = new ActivityLog();
                                    log.setDescription("Payment to Freelancer Successful for Milestone on Contract "+contract.getId());
                                    log.setRequestObject(app.toString(milestone));
                                    log.setResponseObject(app.toString(settlementResponse));
                                    log.setUsername(currentUser.getUserEmail());
                                    log.setUserId(currentUser.getUserUUID());
                                    appLogger.log(log);
                                    //#########################################
                                }catch (Exception ex){
                                    ex.printStackTrace();
                                }
                                //end milestone
                                milestone.setStatus(Status.CO);
                                milestone.setEndDate(new Date());
                                jobMilestoneRepository.save(milestone);
                                //complete project
                                project.setStatus(Status.CO);
                                jobProjectSubmissionRepository.save(project);

                                contract.setLastModifiedDate(new Date());
                                jobContractRepository.save(contract);

                                //fire notification
                                Job currentJob = jobRepository.findById(project.getJobId()).orElse(null);
                                User freelancer=userService.getUserById(project.getUserId());
                                if (currentJob != null) {
                                    NotificationBody body = new NotificationBody();
                                    body.setBody(currentUser.getUserFullName() + " approved the milestone you submitted for " + currentJob.getTitle() + ", and the sum of " + milestone.getAmount()+ " has been released to your account");
                                    body.setSubject("Milestone Completed");
                                    body.setActionType("REDIRECT");
                                    body.setAction("/my-job/contract/milestones/" + project.getJobId() + "/" + project.getProposalId());
                                    body.setTopic("'Job'");
                                    body.setChannel("S");
                                    body.setRecipient(project.getUserId());
                                    body.setRecipientEmail(freelancer.getEmail());
                                    body.setRecipientName(freelancer.getFullName());
                                    notificationSender.pushNotification(body);
                                }

                            }
                            return settlementResponse;

                        } else {
                            logger.info("JOBSERVICE: Escrow transaction failed");
                            return new APIResponse("Escrow transaction failed", false, null);
                        }
                    } else {
                        logger.info("JOBSERVICE: Project request not found");
                        return new APIResponse("Project request history not found", false, null);
                    }
                } else {
                    logger.info("JOBSERVICE: Milestone not found");
                    return new APIResponse("Milestone not found", false, null);
                }
            }
            else {
                logger.info("JOBSERVICE: Contract  not found");
                return new APIResponse("Contract not found", false, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse("Oops! An Error Occurred", false, null);
        }
    }

    public APIResponse settleContractById(String authToken,OAuth2Authentication authentication, String contractReferenceId) throws CloneNotSupportedException {
        try {
            JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
            JobContract contract;
            JobMilestone milestone;
            app.print("Settling Contract Id: " + contractReferenceId);
            contract = jobContractRepository.findByContractReference(contractReferenceId).orElse(null);
            milestone = jobMilestoneRepository.findMilestoneByMilestoneReference(contractReferenceId).orElse(null);
            if (contract == null) {
                if (milestone == null) {
                    return new APIResponse("Contract not found from Reference Id Provided", false, null);
                } else {
                    contract = jobContractRepository.findByContractReference(milestone.getContractReference()).orElse(null);
                }
            }
            app.print(contract);
            app.print(milestone);

            if(contract!=null) {

                if (contract.getStatus().equals(Status.WP) || (milestone != null && milestone.getStatus().equals(Status.WP))) {
                    //check if contract is available
                    JobProposal proposal = jobProposalRepository.findById(contract.getProposalId()).orElse(null);
                    if (proposal == null)
                        return new APIResponse("No Proposal found for this Contract", false, null);
                    //check if job is available
                    Job job = jobRepository.findById(contract.getJobId()).orElse(null);
                    if (job == null)
                        return new APIResponse("No Job history found for this Contract", false, null);

                    app.print("#Contract");
                    app.print(contract);

                    app.print("#milestone");
                    app.print(milestone);

                    double depositedAmount;
                    if (contract.getWorkMethod().equals("Overall"))
                        depositedAmount = contract.getAmount();
                    else if (milestone == null)
                        return new APIResponse("No Milestone found with the reference provided", false, null);
                    else
                        depositedAmount = milestone.getAmount();


                    if (depositedAmount > 0) {

                        double VATRate = 0;
                        double kulaIncomeRate=0;
                        double VATCharge = 0;
                        double kulaIncomeCharge = 0;
                        double pepperestIncomeCharge = 0;
                        double freelancerIncomeAmount=0;
                        int TOTAL_JOBS_COMPLETED = 0;
                        boolean isPaid=false;

                        //###########################
                        // CALCULATE VAT AND CHARGES BASE ON CONFIGS
                        //###########################
                        try {
                            //get configurations
                            List<Config> configs = configService.getConfigs();
                            app.print(configs);
                            if (!configs.isEmpty()) {
                                for (Config currentConfig : configs) {
                                    //getting configs
                                    if (currentConfig.getReference()==ConfigReference.VAT_RATE)
                                        VATRate = Double.valueOf(currentConfig.getValue());
                                    if (currentConfig.getReference()==ConfigReference.KULA_INCOME)
                                        kulaIncomeRate = Double.valueOf(currentConfig.getValue());
                                    if (currentConfig.getReference().equals(ConfigReference.TOTAL_JOBS_COMPLETED))
                                        TOTAL_JOBS_COMPLETED = Integer.valueOf(currentConfig.getValue());

                                    //#getting escrow account
                                    if(currentConfig.getReference().equals(ConfigReference.ESCROW_ACCOUNT_NAME))
                                        this.escrowAccountName=currentConfig.getValue();
                                    if(currentConfig.getReference().equals(ConfigReference.ESCROW_ACCOUNT_NUMBER))
                                        this.escrowAccountNumber=currentConfig.getValue();

                                    //#getting kula income account
                                    if(currentConfig.getReference().equals(ConfigReference.KULA_INCOME_ACCOUNT_NAME))
                                        this.kulaIncomeAccountName =currentConfig.getValue();
                                    if(currentConfig.getReference().equals(ConfigReference.KULA_INCOME_ACCOUNT_NUMBER))
                                        this.kulaIncomeAccountNumber=currentConfig.getValue();

                                    //#getting vat income account
                                    if(currentConfig.getReference().equals(ConfigReference.VAT_INCOME_ACCOUNT_NAME))
                                        this.VATAccountName  =currentConfig.getValue();
                                    if(currentConfig.getReference().equals(ConfigReference.VAT_INCOME_ACCOUNT_NUMBER))
                                        this.VATAccountNumber =currentConfig.getValue();

                                    //#getting vat income account
                                    if(currentConfig.getReference().equals(ConfigReference.PEPPEREST_INCOME_ACCOUNT_NAME))
                                        this.pepperestIncomeAccountName  =currentConfig.getValue();
                                    if(currentConfig.getReference().equals(ConfigReference.PEPPEREST_INCOME_ACCOUNT_NUMBER))
                                        this.pepperestIncomeAccountNumber =currentConfig.getValue();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return new APIResponse("Unable to extract job service configurations", false, null);
                        }
                        app.print(VATRate);
                        app.print(TOTAL_JOBS_COMPLETED);
                        //###########################


                        kulaIncomeCharge=(kulaIncomeRate * depositedAmount) / 100;

                        if (TOTAL_JOBS_COMPLETED < 100000) {
                            pepperestIncomeCharge = 100;
                        } else if (TOTAL_JOBS_COMPLETED >= 100000 && TOTAL_JOBS_COMPLETED < 200000) {
                            pepperestIncomeCharge = 50;
                            kulaIncomeCharge = kulaIncomeCharge + 50;
                        } else if (TOTAL_JOBS_COMPLETED >= 200000 && TOTAL_JOBS_COMPLETED < 300000) {
                            pepperestIncomeCharge = 40;
                            kulaIncomeCharge = kulaIncomeCharge + 60;
                        } else {
                            pepperestIncomeCharge = 30;
                            kulaIncomeCharge = kulaIncomeCharge + 70;
                        }
                        VATCharge = (VATRate * kulaIncomeCharge) / 100;
                        freelancerIncomeAmount = (depositedAmount - pepperestIncomeCharge) - kulaIncomeCharge;
                        kulaIncomeCharge = kulaIncomeCharge - VATCharge;

                        app.print("VAT is "+VATCharge);
                        app.print("kula Income  is "+kulaIncomeCharge);
                        app.print("Freelancer charge is "+freelancerIncomeAmount);
                        app.print("###ESCROW ACCOUNT");
                        app.print(this.escrowAccountName);
                        app.print(this.escrowAccountNumber);
                        app.print("###KULA INCOME ACCOUNT");
                        app.print(this.kulaIncomeAccountName);
                        app.print(this.kulaIncomeAccountNumber);
                        app.print("###PEPPEREST INCOME ACCOUNT");
                        app.print(this.pepperestIncomeAccountName);
                        app.print(this.pepperestIncomeAccountNumber);
                        app.print("###VAT INCOME ACCOUNT");
                        app.print(this.VATAccountName);
                        app.print(this.VATAccountNumber);



                        ArrayList<JobBulkPayment> bulkPaymentStack = new ArrayList<>();
                        String paymentReference = app.makeUIID();
                        String narration = "Settlement from Kula for " + job.getTitle();

                        //Bulk transfers for settlement
                        JobBulkPayment escrowAccount = new JobBulkPayment();
                        escrowAccount.setAccountNumber(escrowAccountNumber);
                        escrowAccount.setAccountName(escrowAccountName);
                        escrowAccount.setNarration(narration);
                        escrowAccount.setExecutedBy(currentUser.getUserUUID());
                        escrowAccount.setExecutedByUsername(currentUser.getUserEmail());
                        escrowAccount.setExecutedFor(contract.getContractReference());
                        escrowAccount.setContractReference(contract.getContractReference());
                        escrowAccount.setPaymentReference(paymentReference);
                        escrowAccount.setCrDrFlag("D");
                        escrowAccount.setAccountType("GL");
                        escrowAccount.setTransactionId("1");
                        escrowAccount.setAmount(0);

                        //***Union Bank***
                        JobBulkPayment kulaAccount = new JobBulkPayment();
                        //###############

                        JobBulkPayment escrowkulaAccountDebit = (JobBulkPayment) app.copy(escrowAccount);
                        escrowkulaAccountDebit.setAmount(kulaIncomeCharge);
                        escrowkulaAccountDebit.setTransactionId("1");
                        escrowkulaAccountDebit.setRemark("Debited by Kula to kula Income account for " + job.getTitle());
                        //##############
                        kulaAccount.setTransactionId("2");
                        kulaAccount.setAccountName(kulaIncomeAccountName);
                        kulaAccount.setAccountNumber(kulaIncomeAccountNumber);
                        kulaAccount.setNarration(narration);
                        kulaAccount.setExecutedByUsername(currentUser.getUserEmail());
                        kulaAccount.setExecutedBy(currentUser.getUserUUID());
                        kulaAccount.setRemark("Credit from Kula to kula Income account for " + job.getTitle());
                        kulaAccount.setExecutedFor(contract.getContractReference());
                        kulaAccount.setContractReference(contract.getContractReference());
                        kulaAccount.setPaymentReference(paymentReference);
                        kulaAccount.setCrDrFlag("C");
                        kulaAccount.setAccountType("GL");
                        kulaAccount.setAmount(kulaIncomeCharge);
                        //############# add to bulk transfer stack ###########
                        bulkPaymentStack.add(escrowkulaAccountDebit);
                        bulkPaymentStack.add(kulaAccount);

                        if (VATCharge > 0) {
                            //***Union Bank***
                            JobBulkPayment VATAccount = new JobBulkPayment();
                            //###############
                            JobBulkPayment escrowVATAccountDebit = (JobBulkPayment) app.copy(escrowAccount);
                            escrowVATAccountDebit.setAmount(VATCharge);
                            escrowVATAccountDebit.setTransactionId("3");
                            escrowVATAccountDebit.setRemark("Debited by Kula to VAT account for " + job.getTitle());
                            //##############
                            VATAccount.setTransactionId("4");
                            VATAccount.setAccountName(VATAccountName);
                            VATAccount.setAccountNumber(VATAccountNumber);
                            VATAccount.setNarration(narration);
                            VATAccount.setExecutedBy(currentUser.getUserUUID());
                            VATAccount.setExecutedByUsername(currentUser.getUserEmail());
                            VATAccount.setRemark("Credit from Kula to VAT account for " + job.getTitle());
                            VATAccount.setExecutedFor(contract.getContractReference());
                            VATAccount.setContractReference(contract.getContractReference());
                            VATAccount.setPaymentReference(paymentReference);
                            VATAccount.setCrDrFlag("C");
                            VATAccount.setAccountType("GL");
                            VATAccount.setAmount(VATCharge);
                            //############# add to bulk transfer stack ###########
                            bulkPaymentStack.add(escrowVATAccountDebit);
                            bulkPaymentStack.add(VATAccount);
                        }


                        //***Pepperest***
                        JobBulkPayment pepperestAccount = new JobBulkPayment();
                        //##############
                        JobBulkPayment escrowPepperestAccountDebit = (JobBulkPayment) app.copy(escrowAccount);
                        escrowPepperestAccountDebit.setAmount(pepperestIncomeCharge);
                        escrowPepperestAccountDebit.setTransactionId("5");
                        escrowPepperestAccountDebit.setRemark("Debited  by Kula for to Pepperest account for " + job.getTitle());
                        //#############
                        pepperestAccount.setTransactionId("6");
                        pepperestAccount.setAccountName(pepperestIncomeAccountName);
                        pepperestAccount.setAccountNumber(pepperestIncomeAccountNumber);
                        pepperestAccount.setNarration(narration);
                        pepperestAccount.setExecutedBy(currentUser.getUserUUID());
                        pepperestAccount.setExecutedByUsername(currentUser.getUserEmail());
                        pepperestAccount.setRemark("Credit from Kula to Pepperest account for " + job.getTitle());
                        pepperestAccount.setExecutedFor(contract.getContractReference());
                        pepperestAccount.setContractReference(contract.getContractReference());
                        pepperestAccount.setPaymentReference(paymentReference);
                        pepperestAccount.setCrDrFlag("C");
                        pepperestAccount.setAccountType("CASA");
                        pepperestAccount.setAmount(pepperestIncomeCharge);
                        //############# add to bulk transfer stack ###########
                        bulkPaymentStack.add(escrowPepperestAccountDebit);
                        bulkPaymentStack.add(pepperestAccount);

                        //***Freelancer***
                        JobBulkPayment freelancerAccount = new JobBulkPayment();
                        //##################
                        JobBulkPayment escrowFreelancerAccountDebit = (JobBulkPayment) app.copy(escrowAccount);
                        escrowFreelancerAccountDebit.setAmount(freelancerIncomeAmount);
                        escrowFreelancerAccountDebit.setTransactionId("7");
                        escrowFreelancerAccountDebit.setRemark("Debited by Kula to Freelancer account for " + job.getTitle());
                        //update escrow account information with the payment for kula

                       if(proposal.getPaymentMethod().equals(PaymentMethod.WALLET)) {
                           //transfer the amount to Kula main account from Wallet
                           PaymentRequest payment = new PaymentRequest();
                           payment.setProposalId(contract.getProposalId());
                           payment.setAmount(freelancerIncomeAmount);
                           payment.setNarration("Credit from Kula to Freelancer account for " + job.getTitle());
                           payment.setDebitAccountName(escrowAccountName);
                           payment.setDebitAccountNumber(escrowAccountNumber);
                           payment.setDebitAccountType("GL");
                           payment.setPaymentReference(paymentReference);
                           payment.setExecutedFor(contractReferenceId);
                           payment.setContractReference(contractReferenceId);
                           payment.setExecutedBy(currentUser.getUserUUID());
                           payment.setExecutedByUsername(currentUser.getUserEmail());
                           app.print("Request:");
                           app.print(payment);
                           APIResponse paymentResponse = jobWalletPaymentService.creditWallet(currentUser, payment);
                           if (paymentResponse.isSuccess()) {
                               isPaid=true;
                               app.print("Wallet payment to freelancer successful");
                               app.print(paymentResponse);
                           }else{
                               app.print("Wallet payment to freelancer failed ");
                               app.print(paymentResponse);
                           }


                       }else{
                           //##################
                           freelancerAccount.setTransactionId("8");
                           freelancerAccount.setAccountName(contract.getFreelancerAccountName());
                           freelancerAccount.setAccountNumber(contract.getFreelancerAccountNumber());
                           freelancerAccount.setNarration(narration);
                           freelancerAccount.setExecutedBy(currentUser.getUserUUID());
                           freelancerAccount.setExecutedByUsername(currentUser.getUserEmail());
                           freelancerAccount.setRemark("Credit from Kula to Freelancer account for " + job.getTitle());
                           freelancerAccount.setExecutedFor(contract.getContractReference());
                           freelancerAccount.setContractReference(contract.getContractReference());
                           freelancerAccount.setPaymentReference(paymentReference);
                           freelancerAccount.setCrDrFlag("C");
                           freelancerAccount.setAccountType("CASA");
                           freelancerAccount.setAmount(freelancerIncomeAmount);
                       }
                        //############# add to bulk transfer stack ###########
                        bulkPaymentStack.add(escrowFreelancerAccountDebit);
                        bulkPaymentStack.add(freelancerAccount);
                        app.print(bulkPaymentStack);

                        APIResponse apiResponse = jobPaymentService.makeBulkPayment(authToken,bulkPaymentStack);
                        if (apiResponse.isSuccess()) {
                            //update contract information
                            contract.setKulaChargeRate(kulaIncomeRate);
                            contract.setVATChargeRate(VATRate);
                            contract.setEscrowCharges(pepperestIncomeCharge);

                            //update job
                            if (contract.getWorkMethod().equals("Overall")) {
                                contract.setIsSettled(true);
                                contract.setSettlementPaymentReferenceB(apiResponse.getPayload().toString());
                                contract.setEndDate(new Date());
                                contract.setLastModifiedDate(new Date());
                                contract.setClearedAmount(freelancerIncomeAmount);
                                contract.setVat(VATCharge);
                                contract.setCharges(kulaIncomeCharge+pepperestIncomeCharge);
                                proposal.setEndDate(new Date());
                                proposal.setStatus(Status.CO);
                                contract.setStatus(Status.CO);
                                job.setStatus(Status.CO);
                                JobProjectSubmission project = jobProjectSubmissionRepository.findSubmittedProjectByProposalAndUserId(proposal.getId(), proposal.getUserId());
                                if (project != null) {
                                    project.setStatus(Status.CO);
                                    jobProjectSubmissionRepository.save(project);
                                }

                            } else {
                                contract.setSettlementPaymentReferenceB(apiResponse.getPayload().toString());
                                contract.setLastModifiedDate(new Date());
                                contract.setClearedAmount(contract.getClearedAmount()+ freelancerIncomeAmount);
                                contract.setVat(contract.getVat()+ VATCharge);
                                contract.setCharges(contract.getCharges()+ kulaIncomeCharge+pepperestIncomeCharge);

                                if (milestone != null) {
                                    milestone.setStatus(Status.CO);
                                    milestone.setEndDate(new Date());
                                    milestone.setClearedAmount(freelancerIncomeAmount);
                                    milestone.setVat(VATCharge);
                                    milestone.setCharges(kulaIncomeCharge+pepperestIncomeCharge);

                                    JobProjectSubmission project = jobProjectSubmissionRepository.findSubmittedProjectByContractReference(milestone.getMilestoneReference());
                                    if (project != null) {
                                        project.setStatus(Status.CO);
                                        jobProjectSubmissionRepository.save(project);
                                    }
                                    jobMilestoneRepository.save(milestone);
                                }
                            }
                            job.setLastModifiedDate(new Date());

                            jobRepository.save(job);
                            jobProposalRepository.save(proposal);
                            jobContractRepository.save(contract);

                            try {
                                //update configurations table
                                Config existingConfig = configService.getConfigByKey(ConfigReference.TOTAL_JOBS_COMPLETED);
                                if (existingConfig != null)
                                    configService.updateConfig(ConfigReference.TOTAL_JOBS_COMPLETED, String.valueOf(Integer.parseInt(existingConfig.getValue()) + 1));
                                else
                                    configService.updateConfig(ConfigReference.TOTAL_JOBS_COMPLETED, String.valueOf(1));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            User freelancer = userService.getUserById(proposal.getUserId());

                            if(freelancer!=null) {
                                NotificationBody body = new NotificationBody();
                                body.setBody("Payment of NGN" + freelancerIncomeAmount + " has successfully  been released to the bank account of " + freelancer.getFullName());
                                body.setSubject("Payment Released to Freelancer");
                                body.setActionType("INFORMATION");
                                body.setTopic("'Job'");
                                body.setChannel("S");
                                body.setPriority("YES");
                                body.setRecipient(currentUser.getUserId());
                                body.setRecipientEmail(currentUser.getUserEmail());
                                body.setRecipientName(currentUser.getUserFullName());
                                notificationSender.pushNotification(body);
                            }

                            if (freelancer != null) {
                                NotificationBody body1 = new NotificationBody();
                                body1.setBody("Payment of NGN" + freelancerIncomeAmount + " posted to your bank account by " + currentUser.getUserFullName() + ".");
                                body1.setSubject("New Payment");
                                body1.setActionType("INFORMATION");
                                body1.setTopic("'Job'");
                                body1.setChannel("S");
                                body1.setPriority("YES");
                                body1.setRecipient(proposal.getEmployerId());
                                body1.setRecipientEmail(freelancer.getEmail());
                                body1.setRecipientName(freelancer.getFullName());
                                notificationSender.pushNotification(body1);
                            }

                            //respond
                            return new APIResponse("Request Successful", true, apiResponse.getPayload());
                        } else {
                            return apiResponse;
                        }

                    } else {
                        return new APIResponse("Contract amount can't be ZERO", false, null);
                    }

                } else {
                    return new APIResponse("Contract not active, it might have  been settled already", false, null);
                }
            }else{
                return new APIResponse("Contract not found", false, null);
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return new APIResponse("Oops! Unable to settle contract", false, null);
        }
    }
}

