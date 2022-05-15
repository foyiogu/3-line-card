package com.unionbankng.future.futuremessagingservice.util;
import com.unionbankng.future.futuremessagingservice.pojos.EmailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSender {

    private static final String EMAIL_DESTINATION = "kulaemailqueue";
    private final JmsTemplate jmsTemplate;
    public void sendEmail(EmailBody emailBody){
        jmsTemplate.convertAndSend(EMAIL_DESTINATION,emailBody);
    }
}
