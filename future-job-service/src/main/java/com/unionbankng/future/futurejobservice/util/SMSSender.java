package com.unionbankng.future.futurejobservice.util;
import com.unionbankng.future.futurejobservice.pojos.SMS;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SMSSender {

    private static final String SMS_DESTINATION = "kulasmsqueue";
    private final App app;
    private final JmsTemplate jmsTemplate;
    public void sendSMS(SMS smsBody){
        try {
            app.print("Sending SMS...");
            app.print(smsBody);
            CachingConnectionFactory connectionFactory = (CachingConnectionFactory) jmsTemplate.getConnectionFactory();
            connectionFactory.setCacheProducers(false);
            jmsTemplate.convertAndSend(SMS_DESTINATION, smsBody);
            app.print("Sent");
        }catch (Exception ex){
            System.out.println("Unable to send SMS");
            ex.printStackTrace();
        }
    }
}
