package com.unionbankng.future.paymentservice.pojos;

import lombok.Data;

public @Data class PaystackDetailUpdate {
	
	private Long id;
	private String uuid;
	private String lastDigit;
	private String cardType;
	private String bankName;
	private String authCode;
	private String signature;
}
