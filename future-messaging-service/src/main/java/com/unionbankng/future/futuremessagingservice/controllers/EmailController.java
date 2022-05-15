package com.unionbankng.future.futuremessagingservice.controllers;

import com.unionbankng.future.futuremessagingservice.entities.ContactUs;
import com.unionbankng.future.futuremessagingservice.interfaces.EmailProvider;
import com.unionbankng.future.futuremessagingservice.pojos.APIResponse;
import com.unionbankng.future.futuremessagingservice.pojos.EmailBody;
import com.unionbankng.future.futuremessagingservice.services.UBNEmailService;
import com.unionbankng.future.futuremessagingservice.util.App;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
public class EmailController {

    private final App app;
    private final UBNEmailService ubnEmailService;

    @PostMapping(value = "/v1/email/send_email")
    public ResponseEntity<APIResponse> sendEmail(@RequestBody EmailBody body) throws Exception {
        try {

            app.print("Sending Email....");
            if(body!=null) {
                ubnEmailService.sendEmail(body);
                app.print("Email sent out");
            }else{
                app.print("Failed to sent the Email");
            }
        } catch (Exception e) {
            app.print("Unable to send Email");
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(new APIResponse("Message sent",true,null));
    }

    @PostMapping(value = "/v1/email/send_contactus")
    public ResponseEntity<APIResponse> contactUs(@RequestBody ContactUs contactUs) throws Exception {
        try {
            app.print("Sending contact us message....");
            if (contactUs != null) {
                APIResponse apiResponse = ubnEmailService.contactUs(contactUs);
                if (apiResponse.isSuccess())
                    app.print("Contact us message sent out");
                return ResponseEntity.ok().body(apiResponse);
            } else {
                app.print("Failed to contact us");
                return ResponseEntity.ok().body(new APIResponse("Failed to contact us", false, null));
            }
        } catch (Exception e) {
            app.print("Unable to send Email");
            e.printStackTrace();
            return ResponseEntity.ok().body(new APIResponse("Unable to send Email", false, null));
        }
    }
}
