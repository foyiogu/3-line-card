package com.threeline.authorizationserver.retrofitservices;

import com.threeline.authorizationserver.pojos.WalletAuthResponse;
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


}
