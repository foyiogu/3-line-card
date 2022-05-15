package com.unionbankng.future.paymentservice.pojos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletGenericResponse {
	private String message;
	private String code;
	private BigDecimal balance;
	private Boolean success;
}