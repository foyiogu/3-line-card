package com.unionbankng.future.paymentservice.controllers;

import com.unionbankng.future.paymentservice.pojos.*;
import com.unionbankng.future.paymentservice.services.WalletService;
import com.unionbankng.future.paymentservice.utils.App;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final App app;


    @GetMapping("/test")
    public String test() {
        System.out.println("test");
        return "Hello World";
    }

    @PostMapping("/verify-interswitch-transaction/{transactionId}")
    public ApiResponse<VerifyTransactionResponse> handleInterswitchVerifyTransaction(@Valid @PathVariable String transactionId)  {
        app.print("VerifyInterswitch Transaction: " + transactionId);
        return walletService.verifyTransaction(transactionId);
    }

    @PostMapping("/debit-wallet")
    public WalletGenericResponse instantGlTransferDebitWallet(@RequestBody WalletDebitRequest debitWalletRequest) {
        return walletService.debitWallet(debitWalletRequest);
    }


    @GetMapping("/resolve-bank-account")
	public ApiResponse validateBankAccount(@RequestParam("accountNo") String accountNo,
                                           @RequestParam("bankCode") String bankCode)  {
        app.print("Validating Account details from Wallet Service");
		return walletService.validateBankAccount(accountNo, bankCode);
	}


    @GetMapping("/get_balance")
    public WalletGenericResponse getWalletBalance(@RequestParam String walletId, @RequestParam String currencyCode) {
        return walletService.getWalletBalance(walletId, currencyCode);
    }


    @GetMapping("/wallet-logs/{walletId}")
    public ApiResponse<List<XLog>> findByWalletId(@PathVariable String walletId) {
        return walletService.fetchLogsByWalletId(walletId);
    }


    @GetMapping("/interswitch-bank-list")
	public ApiResponse<InterswitchBankResponse> getBankList()  {
		return walletService.getBankList();
	}
}
