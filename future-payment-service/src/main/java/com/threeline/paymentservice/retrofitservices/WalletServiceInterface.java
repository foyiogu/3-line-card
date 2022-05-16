package com.threeline.paymentservice.retrofitservices;


import com.threeline.paymentservice.entities.Payment;
import com.threeline.paymentservice.pojos.APIResponse;
import com.threeline.paymentservice.pojos.PaymentDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WalletServiceInterface {


    @POST("wallet/create")
    @Headers({
            "Accept: application/json"
    })
    Call<APIResponse<PaymentDTO>> settlePaymentToWallets(@Body PaymentDTO payment);


}
