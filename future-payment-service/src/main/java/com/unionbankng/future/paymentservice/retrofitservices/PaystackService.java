package com.unionbankng.future.paymentservice.retrofitservices;

import com.unionbankng.future.paymentservice.pojos.VerifyTransactionResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PaystackService {

    @GET("transaction/verify/{reference}")
    Call<VerifyTransactionResponse> verifyTransaction(@Path("reference") String reference);
}
