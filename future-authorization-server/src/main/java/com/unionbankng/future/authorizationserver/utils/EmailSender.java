package com.unionbankng.future.authorizationserver.utils;
import com.unionbankng.future.authorizationserver.pojos.APIResponse;
import com.unionbankng.future.authorizationserver.pojos.EmailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSender {

    private final App app;

    public void sendEmail(EmailBody emailBody) {
        try {
            //todo: send email
            app.print("Email response");
        } catch (Exception ex) {
            System.out.println("Unable to send Email");
            ex.printStackTrace();
        }
    }
}
