package com.unionbankng.future.futuremessagingservice.services;

import com.unionbankng.future.futuremessagingservice.entities.ContactUs;
import com.unionbankng.future.futuremessagingservice.enums.RecipientType;
import com.unionbankng.future.futuremessagingservice.enums.Status;
import com.unionbankng.future.futuremessagingservice.pojos.APIResponse;
import com.unionbankng.future.futuremessagingservice.pojos.EmailAddress;
import com.unionbankng.future.futuremessagingservice.pojos.EmailBody;
import com.unionbankng.future.futuremessagingservice.pojos.UbnEmailResponse;
import com.unionbankng.future.futuremessagingservice.repositories.ContactUsRepository;
import com.unionbankng.future.futuremessagingservice.retrofitservices.UBNEmailServiceInterface;
import com.unionbankng.future.futuremessagingservice.util.App;
import com.unionbankng.future.futuremessagingservice.util.UnsafeOkHttpClient;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@RefreshScope
@Service
@RequiredArgsConstructor
public class UBNEmailService {

    private UBNEmailServiceInterface ubnEmailServiceInterface;
    private final ContactUsRepository contactUsRepository;
    private final UBNAuthService ubnAuthService;
    private final App app;
    @Value("${unionbankng.base.url}")
    private String ubnBaseURL;
    private final MessageSource messageSource;
    private final TemplateEngine templateEngine;


    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ubnBaseURL)
                .build();
        ubnEmailServiceInterface = retrofit.create(UBNEmailServiceInterface.class);
    }

    public APIResponse sendEmail(EmailBody body) {
        try {
            app.print("Sending Email...");
            String authorization = String.format("Bearer %s", ubnAuthService.getUBNAuthServerToken().getAccess_token());
            // app.print(authorization);
            Response<UbnEmailResponse> responseCall = ubnEmailServiceInterface.sendEmail(authorization, body).execute();
            UbnEmailResponse response = responseCall.body();
            app.print("Response:");
            app.print(response);

            return new APIResponse(responseCall.message(), responseCall.isSuccessful(), response);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse(ex.getMessage(), false, null);
        }
    }

    public APIResponse contactUs(ContactUs contactUs) {
        try {
            EmailBody emailBody = EmailBody.builder().body(messageSource.getMessage("welcome.message", new String[]{contactUs.getMessage()}, LocaleContextHolder.getLocale())
                    ).sender(EmailAddress.builder().displayName(contactUs.getName()).email(contactUs.getEmail()).build()).subject(contactUs.getSubject())
                    .recipients(Arrays.asList(EmailAddress.builder().recipientType(RecipientType.TO).email("support@kula.work").displayName("KULA").build())).build();

            EmailBody body = processEmailTemplate(emailBody);
            String authorization = String.format("Bearer %s", ubnAuthService.getUBNAuthServerToken().getAccess_token());
            ubnEmailServiceInterface.sendEmail(authorization, body);

            contactUs.setStatus(Status.NS);
            contactUs.setCreatedAt( new Date());
            return new APIResponse("Message sent Successfully", true, contactUsRepository.save(contactUs));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse(ex.getMessage(), false, null);
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

            return emailBody;
        }catch (Exception ex){
            System.out.println("Unable to generate email template");
            ex.printStackTrace();
            return null;
        }
    }
}
