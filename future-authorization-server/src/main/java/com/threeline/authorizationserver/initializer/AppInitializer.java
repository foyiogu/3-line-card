package com.threeline.authorizationserver.initializer;

import com.threeline.authorizationserver.enums.Role;
import com.threeline.authorizationserver.pojos.RegistrationRequest;
import com.threeline.authorizationserver.repositories.UserRepository;
import com.threeline.authorizationserver.services.RegistrationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Slf4j
@Data
@Component
@RequiredArgsConstructor
public class AppInitializer implements ApplicationRunner {

    private final RegistrationService registrationService;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {


        if(!userRepository.existsByRole(Role.CLIENT_INSTITUTION)){
            log.info("Creating Dummy Client");
            RegistrationRequest request = new RegistrationRequest(
                    "Client", "Institution", null, "client@institution.com", "CLIENTPA@@dd33WORD",
                    "01000000002");
            registrationService.register(request, Role.CLIENT_INSTITUTION);
        }


        if(!userRepository.existsByRole(Role.CONTRACTING_INSTITUTION)){
            log.info("Creating Dummy Contractor");
            RegistrationRequest request = new RegistrationRequest(
                    "Contracting", "Institution", null, "contracting@institution.com", "CONTRActP@SSW0RD",
                    "01000000001");
            registrationService.register(request, Role.CONTRACTING_INSTITUTION);
        }

    }
}