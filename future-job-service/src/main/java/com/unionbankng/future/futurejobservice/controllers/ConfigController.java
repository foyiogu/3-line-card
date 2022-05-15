package com.unionbankng.future.futurejobservice.controllers;
import com.unionbankng.future.futurejobservice.entities.Config;
import com.unionbankng.future.futurejobservice.pojos.APIResponse;
import com.unionbankng.future.futurejobservice.pojos.ConfigRequest;
import com.unionbankng.future.futurejobservice.services.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
public class ConfigController {

    private final ConfigService configService;

    @PostMapping("/v1/job/config/initialize_configs")
    public ResponseEntity<APIResponse<String>> resetConfigs(){
        return ResponseEntity.ok().body(configService.initializeConfigs());
    }

    @PostMapping("/v1/job/config/update_config")
    public ResponseEntity<APIResponse<List<Config>>> updateConfigs(@RequestBody List<ConfigRequest> configRequest){
        return ResponseEntity.ok().body(configService.updateConfigs(configRequest));
    }

    @GetMapping("/v1/job/config/get_configs")
    public ResponseEntity<APIResponse<List<Config>>> getConfigs(){
        List<Config> configs = configService.getConfigs();
        if(configs!=null) {
            return ResponseEntity.ok().body(new APIResponse<>("Request Successful",true,configService.getConfigs()));
        }else{
            return ResponseEntity.ok().body(new APIResponse<>("No Config Found",false,configService.getConfigs()));
        }
    }
}
