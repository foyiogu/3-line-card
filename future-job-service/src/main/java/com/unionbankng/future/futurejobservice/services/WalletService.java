package com.unionbankng.future.futurejobservice.services;
import com.unionbankng.future.futurejobservice.pojos.*;
import com.unionbankng.future.futurejobservice.retrofitservices.WalletServiceInterface;
import com.unionbankng.future.futurejobservice.util.App;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Value("${kula.walletBaseURL}")
    private String walletBaseURL;

    @Value("${kula.walletUsername}")
    private String username;

    @Value("${kula.walletPassword}")
    private String password;

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
        app.print(walletBaseURL);
        walletServiceInterface = retrofit.create(WalletServiceInterface.class);
    }


    private String computeBasicAuthorization(){
        String details=username+":"+password;
        app.print(details);
        return "Basic " +Base64.getEncoder().encodeToString(details.getBytes());
    }

    public WalletAuthResponse getAuth() {
        try {
            app.print("Generating Wallet access token ....");
            String  basicAuth=computeBasicAuthorization();
            app.print(basicAuth);
            Response<WalletAuthResponse> response = walletServiceInterface.getWalletServiceToken("password",username,password,basicAuth).execute();
            app.print("Response:");
            app.print(response);
            app.print(response.body());
            app.print(response.code());
            if (response.isSuccessful()) {
                return response.body();
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public APIResponse<WalletDebitCreditResponse> creditWallet(WalletCreditRequest request) {
        try {
            app.print("Crediting Wallet....");
            app.print("Request:");
            app.print(request);

            WalletAuthResponse auth= getAuth();
            if(auth!=null) {
                String token = "Bearer " + auth.getAccess_token();
                Response<WalletDebitCreditResponse> response = walletServiceInterface.creditWallet(token,request).execute();
                app.print("Response:");
                app.print(response);
                app.print(response.body());
                app.print(response.code());
                if (response.isSuccessful()) {
                    return  new APIResponse("Request Successful",true, response.body());

                } else {
                    return  new APIResponse(response.message(),false,null);
                }
            }else{
                return  new APIResponse("Unable to generate wallet auth token",false,null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return  new APIResponse("Something went wrong",false,null);

        }
    }

    public APIResponse<WalletDebitCreditResponse> debitWallet(WalletDebitRequest request) {
        try {
            app.print("Crediting Wallet....");
            app.print("Request:");
            app.print(request);
            WalletAuthResponse auth= getAuth();
            if(auth!=null) {
                String token = "Bearer " + auth.getAccess_token();
                Response<WalletDebitCreditResponse> response = walletServiceInterface.debitWallet(token,request).execute();
                app.print("Response:");
                app.print(response);
                app.print(response.body());
                app.print(response.code());
                if (response.isSuccessful()) {
                    return  new APIResponse("Request Successful",true, response.body());

                } else {
                    return  new APIResponse(response.message(),false,null);
                }
            }else{
                return  new APIResponse("Unable to generate wallet auth token",false,null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return  new APIResponse("Something went wrong",false,null);

        }
    }

}

