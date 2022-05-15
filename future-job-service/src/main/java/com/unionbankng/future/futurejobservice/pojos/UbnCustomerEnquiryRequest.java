package com.unionbankng.future.futurejobservice.pojos;

import lombok.Data;

@Data
public class UbnCustomerEnquiryRequest {
	
	private String accountNumber;
	private String accountType;
}