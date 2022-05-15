package com.unionbankng.future.futurejobservice.services;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unionbankng.future.futurejobservice.entities.*;
import com.unionbankng.future.futurejobservice.enums.ConfigReference;
import com.unionbankng.future.futurejobservice.enums.LoggingOwner;
import com.unionbankng.future.futurejobservice.enums.Status;
import com.unionbankng.future.futurejobservice.enums.JobType;
import com.unionbankng.future.futurejobservice.pojos.*;
import com.unionbankng.future.futurejobservice.repositories.*;
import com.unionbankng.future.futurejobservice.util.App;
import com.unionbankng.future.futurejobservice.util.AppLogger;
import com.unionbankng.future.futurejobservice.util.JWTUserDetailsExtractor;
import com.unionbankng.future.futurejobservice.util.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class JobService {

    private final  AppService appService;
    private  final JobRepository jobRepository;
    private  final  ConfigService configService;
    private  final JobProposalRepository jobProposalRepository;
    private  final FileStoreService fileStoreService;
    private final JobTeamRepository teamRepository;
    private final NotificationSender notificationSender;
    private  final  JobTeamDetailsRepository jobTeamDetailsRepository;
    private final MessageSource messageSource;
    private final App app;
    private final AppLogger appLogger;
    private Logger logger = LoggerFactory.getLogger(JobService.class);


    public APIResponse addJob(OAuth2Authentication authentication, String jobData, String teamData, MultipartFile[] supporting_files, MultipartFile[] nda_files) throws IOException {
        try {
            JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
            String supporting_file_names = null;
            String nda_file_names=null;
            Job job = app.getMapper().readValue(jobData, Job.class);
            job.setStatus(Status.AC);


            app.print("Job Request:");
            app.print(job);

            if(job.getBudget()>=1000) {

                app.print("Attached Files");
                app.print("NDA files:" + nda_files.length);
                app.print("Supporting Files:" + supporting_files.length);
                //save files if not null
                if (nda_files.length > 0)
                    nda_file_names = this.fileStoreService.storeFiles(nda_files, "kula-nda");
                if (supporting_files.length > 0)
                    supporting_file_names = this.fileStoreService.storeFiles(supporting_files, "kula");

                //cross verify if attached files processed
                if (nda_file_names != null)
                    job.setNdaFiles(nda_file_names);
                if (supporting_file_names != null)
                    job.setSupportingFiles(supporting_file_names);

                Job savedJob = jobRepository.save(job);
                if (savedJob != null) {

                    if (savedJob.getType() == JobType.TEAMS_PROJECT) {
                        JobTeam team = new ObjectMapper().readValue(teamData, JobTeam.class);
                        team.setStatus(Status.AC);
                        team.setJobId(savedJob.getId());
                        if (team.getSelectedTeam() != null) {
                            for (String teamMemberData : team.getSelectedTeam().split("~")) {
                                logger.info(teamMemberData);
                                if (teamMemberData != null && teamMemberData != "") {
                                    TeamMember teamMember = new ObjectMapper().readValue(teamMemberData, TeamMember.class);
                                    logger.info(teamMember.toString());

                                    if (teamMember != null) {
                                        //get the right user percentage
                                        String percentageValue = teamMember.getPercentage().replaceAll("%", "");
                                        //get end date
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(new Date());
                                        c.add(Calendar.DATE, 7);

                                        //calculate user founds
                                        int percentage = Integer.parseInt(percentageValue);
                                        int money = (int) (savedJob.getBudget() / 100) * percentage;

                                        JobProposal proposal = new JobProposal();
                                        proposal.setUserId(teamMember.getId());
                                        proposal.setJobId(savedJob.getId());
                                        proposal.setStatus(Status.PE);
                                        proposal.setEmployerId(savedJob.getOid());
                                        proposal.setDurationType("D");
                                        proposal.setDuration(Long.valueOf(7));
                                        proposal.setEndDate(c.getTime());
                                        proposal.setStartDate(new Date());
                                        proposal.setLastModifiedDate(new Date());
                                        proposal.setAbout(teamMember.getFullName());
                                        proposal.setFullName(teamMember.getFullName());
                                        proposal.setEmail(teamMember.getEmail());
                                        proposal.setImg(teamMember.getImg());
                                        proposal.setAccountName(teamMember.getAccountName());
                                        proposal.setAccountNumber(teamMember.getAccountNumber());
                                        proposal.setAccountType(teamMember.getAccountType());
                                        proposal.setBranchCode("000");
                                        proposal.setWorkMethod("Overall");
                                        proposal.setPreparedCurrency("NGN");
                                        proposal.setBidAmount(Long.valueOf(money));
                                        proposal.setPercentage(Long.valueOf(percentage));
                                        proposal.setIsApplied(false);
                                        proposal.setCreatedAt(new Date());
                                        JobProposal savedProposal = jobProposalRepository.save(proposal);
                                        if (savedProposal != null) {

                                            JobTeamDetails teamMemberDetails = new JobTeamDetails();
                                            teamMemberDetails.setJobId(savedJob.getId());
                                            teamMemberDetails.setEmployerId(savedJob.getOid());
                                            teamMemberDetails.setUserId(teamMember.getId());
                                            teamMemberDetails.setFullName(teamMember.getFullName());
                                            teamMemberDetails.setEmail(teamMember.getEmail());
                                            teamMemberDetails.setImg(teamMember.getImg());
                                            teamMemberDetails.setStatus(Status.PF);
                                            teamMemberDetails.setProposalId(savedProposal.getId());
                                            teamMemberDetails.setAmount(Long.valueOf(money));
                                            teamMemberDetails.setPercentage(Long.valueOf(percentage));
                                            teamRepository.save(team);
                                            jobTeamDetailsRepository.save(teamMemberDetails);

                                            NotificationBody body = new NotificationBody();
                                            body.setBody("Hello! " + teamMemberDetails.getFullName() + ", you have been invited to work on " + savedJob.getTitle() + ".");
                                            body.setSubject("Job Invitation");
                                            body.setActionType("REDIRECT");
                                            body.setAction("/job/details/" + savedJob.getId());
                                            body.setTopic("'Job'");
                                            body.setChannel("S");
                                            body.setPriority("YES");
                                            body.setRecipient(teamMemberDetails.getId());
                                            body.setRecipientEmail(teamMemberDetails.getFullName());
                                            body.setRecipientName(teamMemberDetails.getEmail());
                                            notificationSender.pushNotification(body);
                                            logger.info("Notification fired");

                                        }
                                    }
                                }
//
                            }
                        }
                    }
                    //fire notification
                    Job currentJob = jobRepository.findById(savedJob.getId()).orElse(null);
                    if (currentJob != null) {
                        String[] params = {currentJob.getTitle()};
                        String message = messageSource.getMessage("post.job.successful.email-body", params, LocaleContextHolder.getLocale());
                        NotificationBody body = new NotificationBody();
                        body.setBody(message);
                        body.setSubject("Job Published");
                        body.setActionType("REDIRECT");
                        body.setAction("/job/details/" + savedJob.getId());
                        body.setTopic("'Job'");
                        body.setChannel("S");
                        body.setPriority("YES");
                        body.setRecipient(savedJob.getOid());
                        body.setRecipientEmail(currentUser.getUserEmail());
                        body.setRecipientName(currentUser.getUserFullName());
                        notificationSender.pushNotification(body);
                        logger.info("Notification fired");
                    } else {
                        logger.info("Unable to fire notifications");
                    }
                    //end

                    try {
                        //update configurations table
                        Config existingConfig = configService.getConfigByKey(ConfigReference.TOTAL_JOBS);
                        if (existingConfig != null)
                            configService.updateConfig(ConfigReference.TOTAL_JOBS, String.valueOf(Integer.parseInt(existingConfig.getValue()) + 1));
                        else
                            configService.updateConfig(ConfigReference.TOTAL_JOBS, String.valueOf(1));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                    try {
                        //############### Activity Logging ###########
                        ActivityLog log = new ActivityLog();
                        log.setDescription("Created new Job");
                        log.setRequestObject(app.toString(job));
                        log.setRequestObject(app.toString(savedJob));
                        log.setUsername(currentUser.getUserEmail());
                        log.setUserId(currentUser.getUserUUID());
                        appLogger.log(log);
                        //#########################################
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    return  new APIResponse("Request Successful",true, savedJob);

                } else {
                    logger.info("JOBSERVICE: Unable to save Job");
                    return  new APIResponse("Unable to create Job",false, null);
                }
            }else{
                return  new APIResponse("Job Budget can't be less than 1,000 Naira",false, null);
            }
        }catch ( Exception e){
            e.printStackTrace();
            return  new APIResponse(e.getMessage(),false, null);
        }
    }


    public Job closeJobById(OAuth2Authentication authentication, Long id, int state){
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        Job job =jobRepository.findById(id).orElse(null);
        if(job!=null) {
            if (state == 1)
                job.setStatus(Status.CO);
            else
                job.setStatus(Status.IA);

            job.setLastModifiedDate(new Date());

            NotificationBody body = new NotificationBody();
            body.setBody("Your job for " + job.getTitle() + " has been closed");
            body.setSubject("Job Closed");
            body.setActionType("REDIRECT");
            body.setAction("/job/details/" + job.getId());
            body.setTopic("'Job'");
            body.setChannel("S");
            body.setPriority("NORMAL");
            body.setRecipient(job.getOid());
            body.setRecipientEmail(currentUser.getUserEmail());
            body.setRecipientName(currentUser.getUserFullName());
            notificationSender.pushNotification(body);
            logger.info("Notification fired");
            try {
                //############### Activity Logging ###########
                ActivityLog log = new ActivityLog();
                log.setDescription("Closed Job");
                log.setRequestObject(String.valueOf(id));
                log.setRequestObject(app.toString(job));
                log.setUsername(currentUser.getUserEmail());
                log.setUserId(currentUser.getUserUUID());
                appLogger.log(log);
                //#########################################
            }catch (Exception ex){
                ex.printStackTrace();
            }

            return jobRepository.save(job);
        }else{
            logger.info("JOBSERVICE: Job not found");
            return  null;
        }
    }

    public Job repeatJobById(OAuth2Authentication authentication, Long id){
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);
        Job job =jobRepository.findById(id).orElse(null);
        if(job!=null) {

            Job newJob =(Job)app.copy(job);
            newJob.setStatus(Status.AC);
            newJob.setCreatedAt(new Date());
            newJob.setId(null);

            Job saveAsNewJob =jobRepository.save(newJob);
            //fire notification
            if(saveAsNewJob!=null) {

                try {
                    //update configurations table
                    Config existingConfig = configService.getConfigByKey(ConfigReference.TOTAL_JOBS);
                    if (existingConfig != null)
                        configService.updateConfig(ConfigReference.TOTAL_JOBS, String.valueOf(Integer.parseInt(existingConfig.getValue()) + 1));
                    else
                        configService.updateConfig(ConfigReference.TOTAL_JOBS, String.valueOf(1));
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                NotificationBody body = new NotificationBody();
                body.setBody("Your job for "+newJob.getTitle()+" has been Re-published");
                body.setSubject("Job Published");
                body.setActionType("REDIRECT");
                body.setAction("/job/details/"+job.getId());
                body.setTopic("'Job'");
                body.setChannel("S");
                body.setPriority("NORMAL");
                body.setRecipient(job.getOid());
                body.setRecipientEmail(currentUser.getUserEmail());
                body.setRecipientName(currentUser.getUserFullName());
                notificationSender.pushNotification(body);
            }
            try {
                //############### Activity Logging ###########
                ActivityLog log = new ActivityLog();
                log.setDescription("Repeat Job");
                log.setRequestObject(String.valueOf(id));
                log.setRequestObject(app.toString(job));
                log.setUsername(currentUser.getUserEmail());
                log.setUserId(currentUser.getUserUUID());
                appLogger.log(log);
                //#########################################
            }catch (Exception ex){
                ex.printStackTrace();
            }

            //end
            return  jobRepository.save(job);
        }else{
            logger.info("JOBSERVICE: Job not found");
            return  null;
        }
    }
    public void  deleteJobById(OAuth2Authentication authentication, Long id) {
        JwtUserDetail currentUser = JWTUserDetailsExtractor.getUserDetailsFromAuthentication(authentication);

        try {
            //update configurations table
            Config existingConfig = configService.getConfigByKey(ConfigReference.TOTAL_JOBS);
            if (existingConfig != null)
                configService.updateConfig(ConfigReference.TOTAL_JOBS, String.valueOf(Integer.parseInt(existingConfig.getValue()) -1));
            else
                configService.updateConfig(ConfigReference.TOTAL_JOBS, String.valueOf(0));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        try {
            //############### Activity Logging ###########
            ActivityLog log = new ActivityLog();
            log.setDescription("Repeat Job");
            log.setRequestObject(String.valueOf(id));
            log.setUsername(currentUser.getUserEmail());
            log.setUserId(currentUser.getUserUUID());
            appLogger.log(log);
            //#########################################
        }catch (Exception ex){
            ex.printStackTrace();
        }

        jobRepository.deleteById(id);
    }
    public Model findJobById(Long id, Model model) {
        Job job = jobRepository.findById(id).orElseThrow(  ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        return appService.getJob(job, model);
    }
    public Model getJobByInvitationId(String  invitationId, Model model) {
        Job job = jobRepository.findJobByInvitationId(invitationId).orElseThrow(  ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        return appService.getJob(job, model);
    }

    public Model getJobs(Pageable pageable, Model model){
        Page<Job> paginatedData=jobRepository.findAll(pageable);
        Model jobList=appService.getJobCollection(paginatedData,model).addAttribute("currentPage",pageable.getPageNumber());
        return jobList;
    }
    public Model findJobsByOwnerId(Long id,Pageable pageable, Model model) {
        Page<Job> paginatedData= jobRepository.findByOid(pageable, id);
        Model jobList=appService.getJobCollection(paginatedData,model).addAttribute("currentPage",pageable.getPageNumber());
        return jobList;
    }
    public Model findJobsByUserIdAndStatus(Long id,String status, Pageable pageable, Model model) {
        Page<Job> paginatedData= jobRepository.findJobsByUserIdAndStatus(pageable, id, status);
        Model jobList=appService.getJobCollection(paginatedData,model).addAttribute("currentPage",pageable.getPageNumber());
        return jobList;
    }
    public Model findJobsByOwnerIdAndStatus(Long id,String status, Pageable pageable, Model model) {
        Page<Job> paginatedData= jobRepository.findJobsByOwnerIdAndStatus(pageable, id, status);
        Model jobList=appService.getJobCollection(paginatedData,model).addAttribute("currentPage",pageable.getPageNumber());
        return jobList;
    }

    public Model findJobsByType(JobType type, Pageable pageable, Model model) {
        Page<Job> paginatedData= jobRepository.findByType(pageable, type.name());
        Model jobList=appService.getJobCollection(paginatedData,model).addAttribute("currentPage",pageable.getPageNumber());
        return jobList;
    }

    public Model findJobsByTypeAndCategory(JobType type, String category, Pageable pageable, Model model) {
        Page<Job> paginatedData= jobRepository.findByTypeAndCategory(pageable, type.name(),category);
        Model jobList=appService.getJobCollection(paginatedData,model).addAttribute("currentPage",pageable.getPageNumber());
        return jobList;
    }
    public Model searchJobs(JobType type, String question, Pageable pageable, Model model) {
        Page<Job> paginatedData= jobRepository.findBySearch(pageable, question,type.name());
        Model jobList=appService.getJobCollection(paginatedData,model).addAttribute("currentPage",pageable.getPageNumber());
        return jobList;
    }

    public Long getTotalJobPostedByUserId(Long userId){
        return  jobRepository.getTotalJobPostedByUserId(userId).orElse(0l);
    }
}
