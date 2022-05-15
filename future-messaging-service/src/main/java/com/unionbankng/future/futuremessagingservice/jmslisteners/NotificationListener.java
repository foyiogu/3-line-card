package com.unionbankng.future.futuremessagingservice.jmslisteners;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unionbankng.future.futuremessagingservice.pojos.NotificationBody;
import com.unionbankng.future.futuremessagingservice.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;


@Component
@RequiredArgsConstructor
public class NotificationListener {

    private static final String QUEUE_NAME = "kulanotificationqueue";
    private final NotificationService notificationService;
    private final Logger logger = LoggerFactory.getLogger(NotificationListener.class);
    private final TemplateEngine templateEngine;
    private final ObjectMapper mapper;

    @JmsListener(destination = QUEUE_NAME, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(String json) throws JsonProcessingException {

        NotificationBody notificationBody = mapper.readValue(json, NotificationBody.class);
        logger.info("Notification message Received: {}", notificationBody.getSubject());
        try {
            notificationService.pushNotification(notificationBody.getRecipient(),notificationBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
