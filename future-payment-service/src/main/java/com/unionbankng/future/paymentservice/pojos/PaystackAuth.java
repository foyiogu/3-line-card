package com.unionbankng.future.paymentservice.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaystackAuth {
	
	private String authorization_code;
	private String receiver_bank_account_number;
	private String bin;
	private String last4;
	private String exp_month;
	private String exp_year;
	private String channel;
	private String card_type;
	private String bank;
	private String country_code;
	private String brand;
	private String reusable;
	private String signature;
	
	public PaystackAuth() {
		
	}

}
