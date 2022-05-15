package com.unionbankng.future.futurejobservice.util;
import com.unionbankng.future.futurejobservice.pojos.APIResponse;
import com.unionbankng.future.futurejobservice.pojos.EmailBody;
import com.unionbankng.future.futurejobservice.services.UBNEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSender {
//private static final String EMAIL_DESTINATION = "kulaemailqueue";
//    private final JmsTemplate jmsTemplate;
    private final UBNEmailService ubnEmailService;
    private final App app;

    public void sendEmail(EmailBody emailBody) {
        try {
//            CachingConnectionFactory connectionFactory = (CachingConnectionFactory) jmsTemplate.getConnectionFactory();
//            connectionFactory.setCacheProducers(false);
//            jmsTemplate.convertAndSend(EMAIL_DESTINATION, emailBody);

            APIResponse response = ubnEmailService.sendEmail(emailBody);
            app.print(response);
        } catch (Exception ex) {
            System.out.println("Unable to send Email");
            ex.printStackTrace();
        }
    }
}
