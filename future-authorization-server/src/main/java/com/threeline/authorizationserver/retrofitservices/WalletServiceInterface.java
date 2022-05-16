package com.threeline.authorizationserver.retrofitservices;

import com.threeline.authorizationserver.pojos.APIResponse;
import com.threeline.authorizationserver.pojos.CreateWalletRequest;
import com.threeline.authorizationserver.pojos.UserDTO;
import com.threeline.authorizationserver.pojos.Wallet;
import retrofit2.Call;
import retrofit2.http.*;

public interface WalletServiceInterface {

    @POST("wallet/create")
    @Headers({
            "Accept: application/json"
    })
    Call<APIResponse<Wallet>> createWallet(@Body UserDTO userDTO);


}