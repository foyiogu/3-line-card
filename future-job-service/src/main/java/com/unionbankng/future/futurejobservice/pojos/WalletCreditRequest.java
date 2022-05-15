package com.unionbankng.future.futurejobservice.pojos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public @Data class WalletCreditRequest {

	private BigDecimal  totalAmountPlusCharges;
	private BigDecimal  totalAmount;
	private String walletId;
	private String currencyCode;
	private String debitAccountType;
	private String debitAccountBranch;
	private String debitAccountName;
	private String debitAccountNumber;
	private String transactionType;
	private String naration;
	private String ref;
	private String tradeReference;

	public WalletCreditRequest() {}

}
