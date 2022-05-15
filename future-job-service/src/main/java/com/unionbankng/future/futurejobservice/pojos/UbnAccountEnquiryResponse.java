package com.unionbankng.future.futurejobservice.pojos;

import lombok.Data;

@Data
public class UbnAccountEnquiryResponse {

	 private String code;
	 private String message;
	 private String accountNumber;
	 private String accountName;
	 private String accountBranchCode;
	 private String customerNumber;
	 private String accountClass;
	 private String accountCurrency;
	 private String accountType;
	 private String availableBalance;
	 private String customerAddress;
	 private String customerEmail;
	 private String customerPhoneNumber;
	 private String reference;
	 private String bvn;

}