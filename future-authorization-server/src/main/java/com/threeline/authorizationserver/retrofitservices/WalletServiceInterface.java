package com.threeline.authorizationserver.retrofitservices;

import com.threeline.authorizationserver.pojos.CreateWalletRequest;
import com.threeline.authorizationserver.pojos.Wallet;
import retrofit2.Call;
import retrofit2.http.*;

public interface WalletServiceInterface {

    @POST("wallet/create")
    @Headers({
            "Accept: application/json"
    })
    Call<Wallet> createWallet(@Body CreateWalletRequest request);


}


//@PostMapping("/create")
//    public ResponseEntity<?> createWallet(@RequestBody CreateWalletRequest request){
//        app.print("...Creating wallet");
//        return new ResponseEntity<>(walletService.createContentCreatorWallet(request), HttpStatus.CREATED);
//    }
//
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<?> getUserWallet(@PathVariable Long userId){
//        app.print("Fetching wallet for user");
//        return new ResponseEntity<>(walletService.findWalletByUserId(userId), HttpStatus.OK);
//    }
//
//
//    @PostMapping("/settle-wallet")
//    public ResponseEntity<?> settlePaymentToWallets(@RequestBody Payment payment){
//        app.print("Settling wallets");
//        return new ResponseEntity<>(walletService.fundWalletsAfterPayment(payment), HttpStatus.OK);
//    }