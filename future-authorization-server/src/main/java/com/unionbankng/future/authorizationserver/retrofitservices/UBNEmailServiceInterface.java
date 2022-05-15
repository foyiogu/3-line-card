package com.unionbankng.future.authorizationserver.retrofitservices;
import com.unionbankng.future.authorizationserver.pojos.EmailBody;
import com.unionbankng.future.authorizationserver.pojos.UbnEmailResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UBNEmailServiceInterface {

    @POST("emailservice-api/secured/sendemailsync")
    Call<UbnEmailResponse> sendEmail(@Header("Authorization") String authorization, @Body EmailBody body);
}
