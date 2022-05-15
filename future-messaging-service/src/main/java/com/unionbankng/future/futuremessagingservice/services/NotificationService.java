package com.unionbankng.future.futuremessagingservice.services;
import com.unionbankng.future.futuremessagingservice.enums.RecipientType;
import com.unionbankng.future.futuremessagingservice.entities.MessagingToken;
import com.unionbankng.future.futuremessagingservice.entities.Notification;
import com.unionbankng.future.futuremessagingservice.enums.NotificationStatus;
import com.unionbankng.future.futuremessagingservice.pojos.NotificationBody;
import com.unionbankng.future.futuremessagingservice.repositories.MessagingTokenRepository;
import com.unionbankng.future.futuremessagingservice.repositories.NotificationRepository;
import com.unionbankng.future.futuremessagingservice.util.App;
import com.unionbankng.future.futuremessagingservice.util.EmailSender;
import com.unionbankng.future.futuremessagingservice.pojos.EmailAddress;
import com.unionbankng.future.futuremessagingservice.pojos.EmailBody;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
@RequiredArgsConstructor
public class NotificationService {

    Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private  final MessagingTokenRepository messagingTokenRepository;

    @Value("${google.sidekiq.push_notification_api_key}")
    private String token;
    @Value("${google.sidekiq.push_notification_server_key}")
    private String serverKey;
    @Value("${email.sender}")
    private String emailSenderAddress;
    private String baseURL="https://fcm.googleapis.com/fcm/send";
    @Autowired
    private RestTemplate rest;
    private final EmailSender emailSender;
    private final App app;


    public HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apiKey",token);
        headers.set("Authorization",serverKey);
        return  headers;
    }

    public MessagingToken updateUserMID(Long userId, String umid){
        MessagingToken token=new MessagingToken();
        token.setUserId(userId);
        token.setToken(umid);
        MessagingToken currentToken=messagingTokenRepository.findTokenByUserId(userId);
        if(currentToken!=null) {
            currentToken.setToken(token.getToken());
            return messagingTokenRepository.save(currentToken);
        }
        else {
            return messagingTokenRepository.save(token);
        }
    }

    public Notification pushNotification(Long userId, NotificationBody notificationBody){
         MessagingToken recipient= messagingTokenRepository.findTokenByUserId(notificationBody.getRecipient());

        if(recipient !=null) {
            try {
                app.print("Pushing notification to:");
                app.print(notificationBody);
                app.print("App Token:"+recipient.getToken());

                if(notificationBody.getActionType()==null)
                    notificationBody.setActionType("REDIRECT");
                if(notificationBody.getPriority()==null)
                    notificationBody.setPriority("NORMAL");

                //prepare traditional notification
                Notification traditionalNotification = new Notification();
                traditionalNotification.setSource(userId);
                traditionalNotification.setDestination(recipient.getId());
                traditionalNotification.setMessage(notificationBody.getBody());
                traditionalNotification.setAttachment(notificationBody.getAttachment());
                traditionalNotification.setStatus(NotificationStatus.NS);
                traditionalNotification.setAction(notificationBody.getAction());
                traditionalNotification.setActionType(notificationBody.getActionType());
                traditionalNotification.setPriority(notificationBody.getPriority());
                traditionalNotification.setTopic(notificationBody.getTopic());
                traditionalNotification.setSubject(notificationBody.getSubject());
                traditionalNotification.setChannel(notificationBody.getChannel());

                if(recipient.getToken()!=null) {
                    //prepare push  notification
                    Map<String, Object> pushNotification = new HashMap<>();
                    Map<String, Object> pushBody = new HashMap<>();
                    pushBody.put("title", notificationBody.getSubject());
                    pushBody.put("body", notificationBody.getBody());
                    pushBody.put("action", notificationBody.getAction());
                    pushBody.put("actionType", notificationBody.getActionType().toString());
                    pushBody.put("priority", notificationBody.getPriority());
                    pushBody.put("topic", notificationBody.getTopic());
                    pushBody.put("icon", "./favicon.ico");
                    pushNotification.put("notification", pushBody);
                    pushNotification.put("to", recipient.getToken());

                    HttpEntity<Object> requestEntity = new HttpEntity<Object>(pushNotification, this.getHeaders());
                    ResponseEntity<String> response = rest.exchange(baseURL, HttpMethod.POST, requestEntity, String.class);
                    if (!response.getStatusCode().is2xxSuccessful()) {
                        logger.info("Unable to fire push notification");
                        logger.error(response.getBody());
                    }

                }
                return notificationRepository.save(traditionalNotification);

            }catch (Exception e){
                logger.info("Unable to fire push notification");
                e.printStackTrace();
                return  null;
            }
        }
        else{
            //send an email for priority notifications
            app.print("InApp notification token not fund");
            app.print("Sending Notification to Email.....");
            app.print(notificationBody.getAttachment());
            EmailBody emailBody = EmailBody.builder().body(notificationBody.getBody()
            ).sender(EmailAddress.builder().displayName("Kula Team").email(emailSenderAddress).build()).subject(notificationBody.getSubject())
                    .recipients(Arrays.asList(EmailAddress.builder().recipientType(RecipientType.TO).email(notificationBody.getRecipientEmail()).displayName(notificationBody.getRecipientName()).build())).build();
            
            emailSender.sendEmail(emailBody);
            logger.info("Message Queued successfully");
            logger.info("Recipient not found");
            return  null;
        }
    }
    public Notification markNotificationAsSeen(Long id){
        Notification notification=notificationRepository.findById(id).orElse(null);
        if(notification!=null)
            notification.setStatus(NotificationStatus.SE);
        return notificationRepository.save(notification);
    }

    public boolean markAllAsSeen(Long id){
        notificationRepository.updateAllAsSeen(id);
        return  true;
    }
    public boolean clearAllNotifications(Long id){
          notificationRepository.clearAll(id);
          return  true;
    }

    public Notification findNotificationById(Long id){
        return notificationRepository.findById(id).orElse(null);
    }
    public List<Notification> findAllNotifications(Long id, int from, int limit){
        return  notificationRepository.findAllNotifications(id,from,limit);
    }
    public List<Notification> findTopNotifications(Long id, int limit){
        return  notificationRepository.findTopNotifications(id,limit);
    }
    public List<Notification> findAllNotificationsByStatus(Long id, String status){
        return  notificationRepository.findNotificationsByStatus(id,status.toUpperCase());
    }
    public List<Notification> findTopNotificationsByPriority(Long id, String priority){
        return  notificationRepository.findAllNotSeenNotificationsByPriority(id,priority.toUpperCase());
    }
    public Map<String,Object> getNotificationBase(Long id){
        Map<String, Object> notifications=new HashMap<>();
        notifications.put("top",notificationRepository.findTopNotifications(id,20));
        notifications.put("seen",notificationRepository.findNotificationsByStatus(id,"SE"));
        notifications.put("unseen",notificationRepository.findNotificationsByStatus(id, "NS"));
        notifications.put("all",notificationRepository.findAllNotifications(id,0,50));
        notifications.put("important",notificationRepository.findAllNotSeenNotificationsByPriority(id,"IMPORTANT"));
        return  notifications;
    }
}
