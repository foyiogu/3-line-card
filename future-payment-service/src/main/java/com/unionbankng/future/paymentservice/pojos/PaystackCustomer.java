package com.unionbankng.future.paymentservice.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaystackCustomer {

	private String id;
	private String first_name;
	private String last_name;
	private String email;
	private String customer_code;
	private String phone;
	private String metadata;
	private String risk_action;
    	
}
