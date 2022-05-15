package com.unionbankng.future.futurejobservice.services;
import com.unionbankng.future.futurejobservice.entities.JobTeamDetails;
import com.unionbankng.future.futurejobservice.repositories.JobTeamDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobTeamDetailsService implements Serializable {

    private final JobTeamDetailsRepository jobTeamDetailsRepository;
    public List<JobTeamDetails> findTeamsByJobId(Long jobId){
        return jobTeamDetailsRepository.findByJobId(jobId);
    }

}
