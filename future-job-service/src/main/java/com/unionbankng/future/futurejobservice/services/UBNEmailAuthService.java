package com.unionbankng.future.futurejobservice.services;
import com.unionbankng.future.futurejobservice.pojos.UBNAuthServerTokenResponse;
import com.unionbankng.future.futurejobservice.retrofitservices.UBNAuthAPIServiceInterface;
import com.unionbankng.future.futurejobservice.util.App;
import com.unionbankng.future.futurejobservice.util.UnsafeOkHttpClient;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@RefreshScope
@Service
@RequiredArgsConstructor
public class UBNEmailAuthService {

    // This class was created to handle the authentication of the email service while using UBN API with credentials
    // which might be different from that of the other services

    private UBNAuthAPIServiceInterface ubnAuthAPIServiceInterface;
    private final App app;
    @Value("${production.unionbankng.base.url}")
    private String ubnBaseURL;

    @Value("#{${production.unionbankng.credentials}}")
    private Map<String, String> credentials;

    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ubnBaseURL)
                .build();
        ubnAuthAPIServiceInterface = retrofit.create(UBNAuthAPIServiceInterface.class);
    }


    public UBNAuthServerTokenResponse getUBNAuthServerToken() throws IOException {
        Call<UBNAuthServerTokenResponse> responseCall = ubnAuthAPIServiceInterface.getAuthServerToken(credentials.get("username"),credentials.get("password"),credentials.get("clientSecret"),
                credentials.get("grantType"),credentials.get("clientId"));
        return responseCall.execute().body();
    }


}