package com.threeline.futurewalletservice.services;

import com.threeline.futurewalletservice.entities.Wallet;
import com.threeline.futurewalletservice.entities.WalletHistory;
import com.threeline.futurewalletservice.enums.Currency;
import com.threeline.futurewalletservice.enums.Status;
import com.threeline.futurewalletservice.enums.WalletOwnerRole;
import com.threeline.futurewalletservice.pojos.CreateWalletRequest;
import com.threeline.futurewalletservice.pojos.Payment;
import com.threeline.futurewalletservice.pojos.User;
import com.threeline.futurewalletservice.repositories.WalletHistoryRepository;
import com.threeline.futurewalletservice.repositories.WalletRepository;
import com.threeline.futurewalletservice.util.App;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletHistoryRepository walletHistoryRepository;
    private final App app;


    public Wallet findWallet(Long id) {
        return walletRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
    }

    public Wallet findWalletByUserId(Long userId) {
        return walletRepository.findById(userId).orElse(
                createWalletForUser(userId)
        );
    }

    private Wallet createWalletForUser(Long userId) {
        //TODO: Fetch user details from auth server
        //TODO: Create wallet for user
        return null;
    }

    private Wallet createWalletForUser(User user) {
        //TODO: Fetch user details from auth server
        //TODO: Create wallet for user
        return null;
    }


    public Wallet createContentCreatorWallet(CreateWalletRequest request) {

        if (!walletRepository.existsByUserId(request.getUserId())) {
            Wallet wallet = Wallet.builder()
                    .userId(request.getUserId())
                    .userUuid(request.getUserUuid())
                    .accountName(request.getAccountName())
                    .accountNumber(app.generateAccountNumber())
                    .userEmail(request.getEmail())
                    .balance(BigDecimal.ZERO)
                    .currency(Currency.NGN)
                    .isBlocked(false)
                    .walletOwnerRole(WalletOwnerRole.CONTENT_CREATOR)
                    .build();

            return walletRepository.save(wallet);
        }else {
            throw new IllegalArgumentException("User already has a wallet");
        }
    }


    public boolean fundWalletsAfterPayment(Payment payment){

        Wallet clientInstitutionWallet = walletRepository
                .findByWalletOwnerRole(WalletOwnerRole.CLIENT_INSTITUTION)
                .orElse(createWalletForInstitution(WalletOwnerRole.CLIENT_INSTITUTION));

        Wallet contractingInstitutionWallet = walletRepository
                .findByWalletOwnerRole(WalletOwnerRole.CONTRACTING_INSTITUTION)
                .orElse(createWalletForInstitution(WalletOwnerRole.CONTRACTING_INSTITUTION));

        Wallet creatorWallet = walletRepository.findByUserId(payment.getProductCreatorId())
                .orElse(createWalletForUser(payment.getProductCreatorId()));

        BigDecimal tenPercent = payment.getAmountPaid().divide(BigDecimal.TEN, RoundingMode.HALF_EVEN);
        clientInstitutionWallet.setBalance(clientInstitutionWallet.getBalance().add(tenPercent));
        walletRepository.save(clientInstitutionWallet);
        saveWalletTransactionHistory(clientInstitutionWallet, payment, tenPercent);

        BigDecimal fivePercent = payment.getAmountPaid().divide(BigDecimal.valueOf(20), RoundingMode.HALF_EVEN);
        contractingInstitutionWallet.setBalance(clientInstitutionWallet.getBalance().add(fivePercent));
        walletRepository.save(contractingInstitutionWallet);
        saveWalletTransactionHistory(contractingInstitutionWallet, payment, fivePercent);

        BigDecimal eightyFivePercent = payment.getAmountPaid().subtract(fivePercent).subtract(tenPercent);
        Objects.requireNonNull(creatorWallet).setBalance(creatorWallet.getBalance().add(eightyFivePercent));
        walletRepository.save(creatorWallet);
        saveWalletTransactionHistory(creatorWallet, payment, eightyFivePercent);

        return true;
    }


    private void saveWalletTransactionHistory(Wallet wallet, Payment payment, BigDecimal amount) {

        WalletHistory walletHistory = WalletHistory.builder()
                .walletId(wallet.getId())
                .userId(wallet.getUserId())
                .amount(amount)
                .accountBalance(wallet.getBalance())
                .paymentReference(payment.getPaymentReference())
                .orderReference(payment.getOrderRef())
                .transactionReference(app.generateTransactionReference())
                .currency(payment.getCurrency())
                .transactionDirection(payment.getTransactionDirection())
                .transactionStatus(Status.SUCCESSFUL)
                .build();

        walletHistoryRepository.save(walletHistory);

    }

    private Wallet createWalletForInstitution(WalletOwnerRole walletOwnerRole) {
        User user = fetchUserByRole(walletOwnerRole);
        return createWalletForUser(user);

    }

    private User fetchUserByRole(WalletOwnerRole walletOwnerRole) {
        //TODO Fetch user by role from authserver
        return null;
    }

}
