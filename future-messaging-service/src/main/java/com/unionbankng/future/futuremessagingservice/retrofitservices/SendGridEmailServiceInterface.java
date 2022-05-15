package com.unionbankng.future.futuremessagingservice.retrofitservices;

import com.unionbankng.future.futuremessagingservice.pojos.SendGridEmailBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface SendGridEmailServiceInterface {

    @POST("v3/mail/send")
    Call<SendGridEmailBody> sendEmail(@Header("Authorization") String authorization, @Body SendGridEmailBody body);

}
