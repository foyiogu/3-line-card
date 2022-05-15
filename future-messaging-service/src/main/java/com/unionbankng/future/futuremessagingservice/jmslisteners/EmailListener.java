package com.unionbankng.future.futuremessagingservice.jmslisteners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unionbankng.future.futuremessagingservice.interfaces.EmailProvider;
import com.unionbankng.future.futuremessagingservice.pojos.APIResponse;
import com.unionbankng.future.futuremessagingservice.pojos.EmailBody;
import com.unionbankng.future.futuremessagingservice.pojos.UbnEmailResponse;
import com.unionbankng.future.futuremessagingservice.services.UBNEmailService;
import com.unionbankng.future.futuremessagingservice.util.App;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import java.util.Calendar;

@Component
@RequiredArgsConstructor
public class EmailListener {

    private static final String QUEUE_NAME = "kulaemailqueue";
    private final Logger logger = LoggerFactory.getLogger(EmailListener.class);
    private final TemplateEngine templateEngine;
    private final UBNEmailService ubnEmailService;
    private final ObjectMapper mapper;
    private final App app;


    @JmsListener(destination = QUEUE_NAME, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(String json) throws JsonProcessingException {

        logger.info("Email received from queue");
        EmailBody emailBody = mapper.readValue(json, EmailBody.class);
        app.print(emailBody);
        logger.info("Received message: {}", emailBody.getSubject());

        try {
            logger.info("Sending Email....");
             EmailBody body= processEmailTemplate(emailBody);
             if(body!=null) {
                APIResponse<UbnEmailResponse> response= ubnEmailService.sendEmail(body);
                 logger.info("Email sent out");
                 app.print("Response:");
                 app.print(response);
             }else{
                 logger.info("Failed to sent the Email");
             }
        } catch (Exception e) {
            logger.info("Unable to send Email");
            e.printStackTrace();
        }
    }

    private EmailBody processEmailTemplate(EmailBody emailBody){

        try {
            Context ctx = new Context(LocaleContextHolder.getLocale());
            if (emailBody.getRecipients().size() == 1) {
                ctx.setVariable("name", "Dear " + emailBody.getRecipients().get(0).getDisplayName()+ ",");
                ctx.setVariable("footname", emailBody.getRecipients().get(0).getDisplayName());
                ctx.setVariable("year", Calendar.getInstance().get(Calendar.YEAR));
            } else {
                ctx.setVariable("name", emailBody.getSubject());
            }

            ctx.setVariable("body", emailBody.getBody());

            final String htmlContent = templateEngine.process("mail/html/system-email.html", ctx);
            emailBody.setBody(htmlContent);
            logger.info("Arranged email template ");

            return emailBody;
        }catch (Exception ex){
            System.out.println("Unable to generate email template");
            ex.printStackTrace();
            return null;
        }
    }

}
