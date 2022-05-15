package com.unionbankng.future.futurejobservice.services;

import com.unionbankng.future.futurejobservice.repositories.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class JobFreelancerRateService implements Serializable {

    private Logger logger = LoggerFactory.getLogger(JobFreelancerRateService.class);


    @Autowired
    private final JobRateRepository jobRateRepository;

    public Long getTotalFreRatesByUser(Long userId){
        return  jobRateRepository.getTotalFreRatesByUser(userId).orElse(0l);
    }
}

