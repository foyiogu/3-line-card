
package com.unionbankng.future.futuremessagingservice.services;
import com.unionbankng.future.futuremessagingservice.pojos.APIResponse;
import com.unionbankng.future.futuremessagingservice.pojos.SendGridEmailBody;
import com.unionbankng.future.futuremessagingservice.retrofitservices.SendGridEmailServiceInterface;
import com.unionbankng.future.futuremessagingservice.util.App;
import com.unionbankng.future.futuremessagingservice.util.UnsafeOkHttpClient;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.annotation.PostConstruct;
import java.io.IOException;

@RefreshScope
@Service
@RequiredArgsConstructor
public class SendGridEmailService {

    Logger logger = LoggerFactory.getLogger(SendGridEmailService.class);
    private SendGridEmailServiceInterface sendGridEmailServiceInterface;
    private final App app;

    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.sendgrid.com")
                .build();
        sendGridEmailServiceInterface = retrofit.create(SendGridEmailServiceInterface.class);
    }

    public APIResponse sendEmail(SendGridEmailBody body) {
        try {

            String authorization = String.format("Bearer %s", "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");
            Response<SendGridEmailBody> responseCall = sendGridEmailServiceInterface.sendEmail(authorization, body).execute();
            return new APIResponse(responseCall.message(), responseCall.isSuccessful(), responseCall.body());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse(ex.getMessage(), false, null);
        }
    }
}