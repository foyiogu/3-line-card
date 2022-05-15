package com.unionbankng.future.futurejobservice.services;
import com.unionbankng.future.futurejobservice.entities.Config;
import com.unionbankng.future.futurejobservice.enums.ConfigReference;
import com.unionbankng.future.futurejobservice.pojos.APIResponse;
import com.unionbankng.future.futurejobservice.pojos.ConfigRequest;
import com.unionbankng.future.futurejobservice.repositories.*;
import com.unionbankng.future.futurejobservice.util.App;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final App app;
    private  final ConfigRepository configRepository;


    public List<Config> getConfigs(){
        return  configRepository.findAll();
    }
    public Config getConfigByKey(ConfigReference reference){
        return  configRepository.findByReference(reference).orElse(null);
    }

    public APIResponse initializeConfigs(){
         configRepository.deleteAll();
        this.updateConfig(ConfigReference.TOTAL_JOBS,"0");
        this.updateConfig(ConfigReference.TOTAL_JOBS_COMPLETED,"0");
        this.updateConfig(ConfigReference.TOTAL_JOBS_REJECTED,"0");
        this.updateConfig(ConfigReference.KULA_INCOME,"5");
        this.updateConfig(ConfigReference.VAT_RATE,"7.5");
        //production escrow
        this.updateConfig(ConfigReference.ESCROW_ACCOUNT_NAME,"PEPPEREST PAYABLE ACCOUNT");
        this.updateConfig(ConfigReference.ESCROW_ACCOUNT_NUMBER,"250990149");
        //production kula income
        this.updateConfig(ConfigReference.KULA_INCOME_ACCOUNT_NAME,"KULA INCOME ACCOUNT");
        this.updateConfig(ConfigReference.KULA_INCOME_ACCOUNT_NUMBER,"315200047");
        //production vat income
        this.updateConfig(ConfigReference.VAT_INCOME_ACCOUNT_NAME,"VAT Account");
        this.updateConfig(ConfigReference.VAT_INCOME_ACCOUNT_NUMBER,"250100012");
        //production pepperest income
        this.updateConfig(ConfigReference.PEPPEREST_INCOME_ACCOUNT_NAME,"Pepper Rest Africa");
        this.updateConfig(ConfigReference.PEPPEREST_INCOME_ACCOUNT_NUMBER,"0145692798");
        List<Config> initiatedConfigs=configRepository.findAll();
        return  new APIResponse("success",true,initiatedConfigs);
    }

    public APIResponse updateConfigs(List<ConfigRequest> requestList) {
        for (ConfigRequest request : requestList) {
            this.updateConfig(request.getName(), request.getValue());
        }
        List<Config> updatedConfigs=configRepository.findAll();
        return new APIResponse("success",true,updatedConfigs);
    }

    public Config updateConfig(ConfigReference reference, String value){
        Config existingConfig=configRepository.findByReference(reference).orElse(null);
        if(existingConfig!=null) {
            existingConfig.setValue(value);
            existingConfig.setCreatedAt(new Date());
            return  configRepository.save(existingConfig);
        }else {
            //create new config
            Config config = new Config();
            config.setUid(app.makeUIID());
            config.setReference(reference);
            config.setValue(value);
            config.setCreatedAt(new Date());
            if (reference.equals(ConfigReference.VAT_RATE) || reference.equals(ConfigReference.KULA_INCOME))
                config.setType("percentage");
            else
                config.setType("value");
            return configRepository.save(config);
        }
    }

}
