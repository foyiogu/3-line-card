package com.unionbankng.future.paymentservice.pojos;

import lombok.Data;

import java.math.BigDecimal;

public @Data class WalletDebitRequest {

	private BigDecimal  totalAmountPlusCharges;
	private BigDecimal  totalAmount;
	private String walletId;
	private String currencyCode;
	private String creditAccountType;
	private String creditAccountName;
	private String creditAccountNumber;
	private String creditAccountBranch;
	private String transactionType;
	private String naration;
	private String ref;
	private String tradeReference;
	public WalletDebitRequest() {}

}
