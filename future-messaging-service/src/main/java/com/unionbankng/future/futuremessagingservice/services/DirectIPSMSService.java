package com.unionbankng.future.futuremessagingservice.services;
import com.unionbankng.future.futuremessagingservice.pojos.APIResponse;
import com.unionbankng.future.futuremessagingservice.pojos.SMS;
import com.unionbankng.future.futuremessagingservice.util.App;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;


@Service
@RequiredArgsConstructor
public class DirectIPSMSService {

    String baseURL="https://websms.ipintegrated.com/HTTPIntegrator_SendSMS_1";
    @Value("${spring.directIP.SMS-senderId}")
    String senderId;
    @Value("${spring.directIP.SMS-username}")
    String username;
    @Value("${spring.directIP.SMS-password}")
    String password;

    @Autowired
    private RestTemplate rest;
    private final App app;



    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setCacheControl("no-cache");
        return headers;
    }

    public APIResponse sendSMS(SMS sms){
        app.print("Sending SMS to "+sms.getRecipient());
        app.print(sms);
        HttpEntity<String> entity = new HttpEntity<String>(this.getHeaders());
        ResponseEntity<String> response = rest.exchange(baseURL + "?u="+username+"&p="+password+"&s="+senderId+"&r=t&f=f&d="+sms.getRecipient()+"&t="+sms.getMessage(), HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            app.print("SMS sent Successfully");
               return  new APIResponse("SMS Sent Successfully",true,response.getBody());
        }else{
            app.print("Unable to send SMS");
            app.print(response.getBody());
            return  new APIResponse("Unable to send the SMS",true,response.getBody());
        }
    }
}
