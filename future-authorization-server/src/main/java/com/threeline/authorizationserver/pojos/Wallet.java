package com.threeline.authorizationserver.pojos;
import com.threeline.authorizationserver.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Wallet {
	private Long id;
	private Long userId;
	private String userUuid;
	private String accountName;
	private String userEmail;
	private BigDecimal balance;
	private Currency currency;
	private String accountNumber;
}