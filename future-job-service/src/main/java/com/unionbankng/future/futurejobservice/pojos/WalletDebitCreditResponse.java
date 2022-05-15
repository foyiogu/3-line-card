package com.unionbankng.future.futurejobservice.pojos;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletDebitCreditResponse {
	private String message;
	private String code;
	private BigDecimal balance;
	private Boolean success;
}