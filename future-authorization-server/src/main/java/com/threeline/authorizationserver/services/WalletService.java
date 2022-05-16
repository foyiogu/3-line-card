package com.threeline.authorizationserver.services;

import com.threeline.authorizationserver.entities.User;
import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.pojos.CreateWalletRequest;
import com.threeline.authorizationserver.pojos.Wallet;
import com.threeline.authorizationserver.retrofitservices.WalletServiceInterface;
import com.threeline.authorizationserver.utils.App;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WalletService implements Serializable {

    private WalletServiceInterface walletServiceInterface;
    private final App app;


    @Value("${walletBaseURL}")
    private String walletBaseURL;


    @PostConstruct
    public void init() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                .baseUrl(walletBaseURL)
                .build();
        walletServiceInterface = retrofit.create(WalletServiceInterface.class);
    }



    public APIResponse<Wallet> createWallet(User user){
        try {
            app.print("Creating Wallet....");
            CreateWalletRequest createWalletRequest = new CreateWalletRequest();
            createWalletRequest.setUserId(user.getId());
            createWalletRequest.setUserUuid(user.getUuid());
            createWalletRequest.setEmail(user.getEmail());
            createWalletRequest.setAccountName(user.getWalletAccountNumber());

                Response<APIResponse<Wallet>> response = walletServiceInterface.createWallet(createWalletRequest).execute();
                app.print("Response:");
                app.print(response);
                app.print(response.body());
                app.print(response.code());
                if (response.isSuccessful()) {
                    return  response.body();

                } else {
                    return  new APIResponse<>(response.message(),false,null);
                }
        } catch (Exception ex) {
            ex.printStackTrace();
            return  new APIResponse<>("Something went wrong",false,null);
        }
    }


}