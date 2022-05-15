package com.unionbankng.future.paymentservice.utils;



import com.unionbankng.future.paymentservice.pojos.VerifyTransactionResponse;
import com.unionbankng.future.paymentservice.retrofitservices.PaystackService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

@Service
public class PaystackApiHandler {

	@Value("${paystack.secret.key}")
	private String paystackSecretKey;
	
	
	PaystackService paystackService;


	public PaystackApiHandler() {

		Retrofit retrofit = new Retrofit.Builder().client(clientWithApiKey(paystackSecretKey))
				.baseUrl("https://api.paystack.co")
				.build();

		paystackService= retrofit.create(PaystackService.class);

	}

	private OkHttpClient clientWithApiKey(String apiKey) {
		return new OkHttpClient.Builder()
				.addInterceptor(chain -> {
					Request originalRequest = chain.request();
					Request request = originalRequest.newBuilder().addHeader(HttpHeaders.AUTHORIZATION,"Bearer " + apiKey).build();
					return chain.proceed(request);
				}).build();
	}

	public Response<VerifyTransactionResponse> verifyTransaction(String transReference) throws IOException {

		 return paystackService.verifyTransaction(transReference).execute();

	}


}
