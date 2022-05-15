package com.unionbankng.future.authorizationserver.retrofitservices;

import com.unionbankng.future.authorizationserver.pojos.CreateWalletRequest;
import com.unionbankng.future.authorizationserver.pojos.WalletAuthResponse;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface WalletServiceInterface {

    @POST("oauth/token")
    @FormUrlEncoded
    @Headers({
            "Accept: application/json"
    })
    Call<WalletAuthResponse> getWalletServiceToken(@Field("grant_type") String grantType,
                                                   @Field("username") String username,
                                                   @Field("password") String password,
                                                   @Header("Authorization") String authorization);


    @POST("api/v1/wallets/create_wallet")
    @Headers({
            "Accept: application/json"
    })
    Call<Map<String, String>> createWallet(@Header("Authorization") String authorization,
                                           @Body CreateWalletRequest createWalletRequest);


}
