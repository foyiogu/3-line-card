package com.threeline.futurewalletservice.controllers;

import com.threeline.futurewalletservice.entities.Wallet;
import com.threeline.futurewalletservice.pojos.APIResponse;
import com.threeline.futurewalletservice.pojos.CreateWalletRequest;
import com.threeline.futurewalletservice.pojos.Payment;
import com.threeline.futurewalletservice.services.WalletService;
import com.threeline.futurewalletservice.util.App;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/wallet")
public class WalletController {

    private final App app;
    private final WalletService walletService;


    @PostMapping("/create")
    public ResponseEntity<APIResponse<Wallet>> createWallet(@RequestBody CreateWalletRequest request){
        app.print("...Creating wallet");
        return new ResponseEntity<>(walletService.createContentCreatorWallet(request), HttpStatus.CREATED);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserWallet(@PathVariable Long userId){
        app.print("Fetching wallet for user");
        return new ResponseEntity<>(walletService.findWalletByUserId(userId), HttpStatus.OK);
    }


    @PostMapping("/settle-wallet")
    public ResponseEntity<?> settlePaymentToWallets(@RequestBody Payment payment){
        app.print("Settling wallets");
        return new ResponseEntity<>(walletService.fundWalletsAfterPayment(payment), HttpStatus.OK);
    }




}
