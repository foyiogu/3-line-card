package com.unionbankng.future.futurejobservice.services;
import com.unionbankng.future.futurejobservice.entities.Test;
import com.unionbankng.future.futurejobservice.repositories.TestRepository;
import com.unionbankng.future.futurejobservice.util.App;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class TestService implements Serializable {

    private final App app;
    private final TestRepository testRepository;

    public Test add(Test test){
        app.print(app);
       return testRepository.save(test);
    }
}
