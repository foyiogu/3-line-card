package com.unionbankng.future.futurejobservice.retrofitservices;

import com.unionbankng.future.futurejobservice.pojos.UBNAuthServerTokenResponse;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UBNAuthAPIServiceInterface {

    @POST("authserv/oauth/token")
    Call<UBNAuthServerTokenResponse> getAuthServerToken(@Query("username") String username, @Query("password") String password,
                                                        @Query("client_secret") String clientSecret, @Query("grant_type") String grantType,
                                                        @Query("client_id") String clientId);

    @POST("ubnmiserv/oauth/token")
    Call<UBNAuthServerTokenResponse> getAccountServerToken(@Query("username") String username, @Query("password") String password,
                                                           @Query("client_secret") String clientSecret, @Query("grant_type") String grantType,
                                                           @Query("client_id") String clientId);

}