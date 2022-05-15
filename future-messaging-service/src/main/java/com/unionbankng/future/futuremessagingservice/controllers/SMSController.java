package com.unionbankng.future.futuremessagingservice.controllers;
import com.unionbankng.future.futuremessagingservice.pojos.APIResponse;
import com.unionbankng.future.futuremessagingservice.pojos.SMS;
import com.unionbankng.future.futuremessagingservice.services.DirectIPSMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
public class SMSController {

    private final DirectIPSMSService directIPSMSService;

    @PostMapping(value = "/v1/sms/send_sms")
    public ResponseEntity<APIResponse> sendSMS(@Valid @RequestBody SMS sms)  {
        return ResponseEntity.ok().body(directIPSMSService.sendSMS(sms));
    }
}
