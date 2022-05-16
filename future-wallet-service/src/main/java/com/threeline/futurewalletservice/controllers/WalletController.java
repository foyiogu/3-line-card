package com.threeline.futurewalletservice.controllers;

import com.threeline.futurewalletservice.entities.Wallet;
import com.threeline.futurewalletservice.pojos.APIResponse;
import com.threeline.futurewalletservice.pojos.CreateWalletRequest;
import com.threeline.futurewalletservice.pojos.PaymentDTO;
import com.threeline.futurewalletservice.services.WalletService;
import com.threeline.futurewalletservice.util.App;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/wallet")
public class WalletController {

    private final App app;
    private final WalletService walletService;


    @PostMapping("/create")
    public APIResponse<Wallet> createWallet(@RequestBody CreateWalletRequest request){
        app.print("...Creating wallet");
        return walletService.createContentCreatorWallet(request);
    }


    @GetMapping("/user/{userId}")
    public APIResponse<Wallet> getUserWallet(@PathVariable Long userId){
        app.print("Fetching wallet for user");
        return new APIResponse<>("success", true, walletService.findWalletByUserId(userId));
    }


    @PostMapping("/settle-wallet")
    public APIResponse<PaymentDTO> settlePaymentToWallets(@RequestBody PaymentDTO payment){
        app.print("Settling wallets");
        return walletService.fundWalletsAfterPayment(payment);
    }




}
