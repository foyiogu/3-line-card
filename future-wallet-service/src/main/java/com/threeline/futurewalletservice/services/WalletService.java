package com.threeline.futurewalletservice.services;

import com.threeline.futurewalletservice.entities.Wallet;
import com.threeline.futurewalletservice.entities.WalletHistory;
import com.threeline.futurewalletservice.enums.Currency;
import com.threeline.futurewalletservice.enums.Status;
import com.threeline.futurewalletservice.enums.Role;
import com.threeline.futurewalletservice.pojos.APIResponse;
import com.threeline.futurewalletservice.pojos.CreateWalletRequest;
import com.threeline.futurewalletservice.pojos.PaymentDTO;
import com.threeline.futurewalletservice.pojos.UserDTO;
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
    private final UserService userService;
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

    public Wallet createWalletForUser(UserDTO userDTO) {
        if (!walletRepository.existsByUserId(userDTO.getId())) {
            Wallet wallet = Wallet.builder()
                    .userId(userDTO.getId())
                    .userUuid(userDTO.getUuid())
                    .accountName(userDTO.toString())
                    .accountNumber(app.generateAccountNumber())
                    .userEmail(userDTO.getEmail())
                    .balance(BigDecimal.ZERO)
                    .currency(Currency.NGN)
                    .isBlocked(false)
                    .role(userDTO.getRole())
                    .build();
            return walletRepository.save(wallet);
        }

        return walletRepository.findByUserId(userDTO.getId()).get();
    }


    public APIResponse<Wallet> createContentCreatorWallet(CreateWalletRequest request) {

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
                    .role(Role.CONTENT_CREATOR)
                    .build();

            return new APIResponse<>("wallet created", true, walletRepository.save(wallet));

        }else {
            throw new IllegalArgumentException("User already has a wallet");
        }
    }


    public APIResponse<PaymentDTO> fundWalletsAfterPayment(PaymentDTO payment){

        Wallet clientInstitutionWallet = walletRepository
                .findByRole(Role.CLIENT_INSTITUTION)
                .orElse(createWalletForInstitution(Role.CLIENT_INSTITUTION));

        Wallet contractingInstitutionWallet = walletRepository
                .findByRole(Role.CONTRACTING_INSTITUTION)
                .orElse(createWalletForInstitution(Role.CONTRACTING_INSTITUTION));

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

        return new APIResponse<>("wallet settled", true, payment);

    }


    private void saveWalletTransactionHistory(Wallet wallet, PaymentDTO payment, BigDecimal amount) {

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

    private Wallet createWalletForInstitution(Role role) {
        UserDTO userDTO = userService.fetchUserByRole(role);
        return createWalletForUser(userDTO);

    }

}
