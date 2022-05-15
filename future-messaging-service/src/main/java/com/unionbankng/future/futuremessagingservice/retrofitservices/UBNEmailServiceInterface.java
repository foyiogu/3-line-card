package com.unionbankng.future.futuremessagingservice.retrofitservices;

import com.unionbankng.future.futuremessagingservice.pojos.EmailBody;
import com.unionbankng.future.futuremessagingservice.pojos.UbnEmailResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UBNEmailServiceInterface {

    @POST("emailservice-api/secured/sendemail")
    Call<UbnEmailResponse> sendEmail(@Header("Authorization") String authorization, @Body EmailBody body);

}
