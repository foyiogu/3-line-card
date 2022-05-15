package com.unionbankng.future.futurejobservice.controllers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.unionbankng.future.futurejobservice.entities.Test;
import com.unionbankng.future.futurejobservice.pojos.*;
import com.unionbankng.future.futurejobservice.entities.JobBulkPayment;
import com.unionbankng.future.futurejobservice.services.JobPaymentService;
import com.unionbankng.future.futurejobservice.services.TestService;
import com.unionbankng.future.futurejobservice.services.WalletService;
import com.unionbankng.future.futurejobservice.util.App;
import com.unionbankng.future.futurejobservice.util.AppLogger;
import com.unionbankng.future.futurejobservice.util.NotificationSender;
import com.unionbankng.future.futurejobservice.util.SMSSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api")
public class AppController {

    private final WalletService walletService;
    private final JobPaymentService jobPaymentService;
    private final NotificationSender notificationSender;
    private final SMSSender smsSender;
    private final TestService testService;
    private final AppLogger appLogger;
    private final App app;

    @GetMapping("/v1/ping")
    public ResponseEntity<APIResponse<String>> pingService(){
        app.print("Pinging....");
        SMS sms= new SMS();
        sms.setRecipient("+2348064160204");
        sms.setMessage("This is just Test");
        smsSender.sendSMS(sms);
        return ResponseEntity.ok().body( new APIResponse("Service is Up", true, "Live"));
    }

    @PostMapping("/v1/test")
    public ResponseEntity<APIResponse<Test>> testService(@Nullable @RequestParam String email){
        NotificationBody body = new NotificationBody();
        body.setBody("Hello Test!, your job CYX has been published on Kula");
        body.setSubject("Job Posted Successfully");
        body.setActionType("REDIRECT");
        body.setAction("/job/details/1");
        body.setTopic("'Job'");
        body.setChannel("S");
        body.setPriority("YES");
        body.setRecipient(1l);
        body.setRecipientEmail(email == null ? "net.rabiualiyu@gmail.com" : email);
        body.setRecipientName("Rabiu Aliyu");
        notificationSender.pushNotification(body);
        app.print("Notification fired");

        return ResponseEntity.ok().body(new APIResponse("Request Successful", true, null));
    }

    @PostMapping("/v1/notification/test")
    public ResponseEntity<APIResponse<String>> testNotificationService(@Nullable @RequestParam String email,  OAuth2Authentication authentication){
        NotificationBody body = new NotificationBody();
        body.setBody("Testing notification");
        body.setSubject("Test");
        body.setActionType("REDIRECT");
        body.setAction("https://example.com");
        body.setTopic("Job");
        body.setChannel("S");
        body.setPriority("YES");
        body.setRecipientEmail(email == null ? "net.rabiualiyu@gmail.com" : email);
        body.setRecipientName("Rabiu Aliyu");
        body.setRecipient(1l);

        notificationSender.pushNotification(body);
        return ResponseEntity.ok().body(new APIResponse("Notification Fired", true, body));
    }

    @PostMapping("/v1/bulk/bank_transfer/test")
    public ResponseEntity<APIResponse<String>> testBulkTransfer(@RequestHeader(value="Authorization") String authToken){

        ArrayList<JobBulkPayment> bulkPayments=new ArrayList<>();
        JobBulkPayment bank1= new JobBulkPayment();
        bank1.setTransactionId("1");
        bank1.setAccountNumber("210210071");
        bank1.setAccountType("GL");
        bank1.setAccountName("CPC CASH DEPOSIT ACCOUNT IMPLEMENT");
        bank1.setNarration("My first testing");
        bank1.setAmount(15);
        bank1.setCrDrFlag("D");
        bank1.setExecutedBy("Test User");
        bank1.setExecutedFor("Test Something");
        bank1.setPaymentReference("test111");
        bulkPayments.add(bank1);

        JobBulkPayment bank2= new JobBulkPayment();
        bank2.setTransactionId("2");
        bank2.setAccountNumber("315200043");
        bank2.setAccountType("GL");
        bank2.setAccountName("WESTERN UNION-COMMISSION");
        bank2.setNarration("My first testing");
        bank2.setAmount(15);
        bank2.setCrDrFlag("C");
        bank2.setExecutedBy("Test User");
        bank2.setExecutedFor("Test Something");
        bank2.setPaymentReference("test111");
        bulkPayments.add(bank2);
        APIResponse response=jobPaymentService.makeBulkPayment(authToken,bulkPayments);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/v1/bank/transfer/test")
    public ResponseEntity<APIResponse<String>> bankTransfer(@RequestHeader(value="Authorization") String authToken) throws JsonProcessingException {
        PaymentRequest payment = new PaymentRequest();
        payment.setAmount(52);
        payment.setNarration("Testing Narration 2");
        payment.setCreditAccountName("GL M36 FLOAT ACCOUNT");
        payment.setCreditAccountNumber("250990140");
        payment.setDebitAccountName("OLANLOKUN LANRE");
        payment.setDebitAccountNumber("0040553624");
        payment.setExecutedBy("Test User");
        payment.setExecutedFor("Test");
        payment.setDebitAccountType("CASA");
        payment.setCreditAccountType("GL");
        payment.setPaymentReference("q898sjdjasid98ejd9w8e");
        APIResponse response = jobPaymentService.makePayment(authToken, payment);
        return ResponseEntity.ok().body(response);

    }

}
