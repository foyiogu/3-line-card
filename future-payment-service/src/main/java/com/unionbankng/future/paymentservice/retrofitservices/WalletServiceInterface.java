package com.unionbankng.future.paymentservice.retrofitservices;

import com.unionbankng.future.paymentservice.pojos.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

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

    @POST("api/v1/wallet/instant_credit")
    @Headers({
            "Accept: application/json"
    })
    Call<WalletGenericResponse> creditWallet(@Header("Authorization") String authorization, @Body WalletCreditRequest request);


    @POST("api/v1/wallet/debit-wallet")
    @Headers({
            "Accept: application/json"
    })
    Call<WalletGenericResponse> debitWallet(@Header("Authorization") String authorization, @Body WalletDebitRequest request);


    @POST("api/v1/interswitch/verify/{transactionId}")
    @Headers({
            "Accept: application/json"
    })
    Call<ApiResponse<VerifyTransactionResponse>> verifyInterswitchTransaction(@Header("Authorization") String authorization,
                                                                 @Path("transactionId") String transactionId);


    @GET("api/v1/interswitch/resolve-bank-account")
    @Headers({
            "Accept: application/json"
    })
    Call<ApiResponse<InterswitchAccountValidationResponse>> validateBankAccount(@Header("Authorization") String authorization,
                                                                 @Query("accountNo") String accountNo,
                                                                 @Query("bankCode") String bankCode);



    @GET("api/v1/wallet/get_balance")
    @Headers({
            "Accept: application/json"
    })
    Call<WalletGenericResponse> getWalletBalance(@Header("Authorization") String authorization,
                                                                                @Query("walletId") String walletId,
                                                                                @Query("currencyCode") String currencyCode);


    @GET("api/v1/wallet_logs/{walletId}")
    @Headers({
            "Accept: application/json"
    })
    Call<ApiResponse<List<XLog>>> fetchLogsByWalletId(@Header("Authorization") String authorization,
                                                      @Path("walletId") String walletId);


    @GET("api/v1/interswitch/bank-codes")
    @Headers({
            "Accept: application/json"
    })
    Call<ApiResponse<InterswitchBankResponse>> getBankInterswitchList(@Header("Authorization") String authorization);



}