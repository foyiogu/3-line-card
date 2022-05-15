package com.threeline.authorizationserver.utils;
import com.threeline.authorizationserver.pojos.EmailBody;
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
