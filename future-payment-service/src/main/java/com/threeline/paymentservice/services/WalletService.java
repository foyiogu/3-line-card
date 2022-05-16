package com.threeline.paymentservice.services;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.threeline.paymentservice.entities.Payment;
import com.threeline.paymentservice.pojos.APIResponse;
import com.threeline.paymentservice.pojos.PaymentDTO;
import com.threeline.paymentservice.retrofitservices.WalletServiceInterface;
import com.threeline.paymentservice.utils.App;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WalletService implements Serializable {

    private Logger logger = LoggerFactory.getLogger(WalletService.class);
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


    public APIResponse<PaymentDTO> settlePaymentToWallets(Payment payment){
        try {
            app.print("Settling Wallets....");
            PaymentDTO paymentDTO = new PaymentDTO();
            BeanUtils.copyProperties(payment, paymentDTO);

            Response<APIResponse<PaymentDTO>> response = walletServiceInterface.settlePaymentToWallets(paymentDTO).execute();
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