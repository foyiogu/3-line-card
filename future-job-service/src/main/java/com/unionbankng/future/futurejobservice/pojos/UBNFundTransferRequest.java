package com.unionbankng.future.futurejobservice.pojos;


import lombok.Data;

@Data
public class UBNFundTransferRequest {
	private String amount;
	private String creditAccountBankCode;
	private String creditAccountBranchCode;
	private String creditAccountName;
	private String creditAccountNumber;
	private String creditAccountType;
	private String creditNarration;
	private String currency;
	private String debitAccountBranchCode;
	private String debitAccountName;
	private String debitAccountNumber;
	private String debitAccountType;
	private String debitInstrumentNumber;
	private String debitNarration;
	private String feeEntryMode;
	private String initBranchCode;
	private String paymentReference;
	private String paymentTypeCode;
	private String TEST22;
	private String valueDate;
	private String channelCode;
	private String beneficiaryBvn;
	private String beneficiaryKycLevel;
	private String senderBvn;
	private String senderKycLevel;
	private String nameEnquirySessionId;
	private String ftSessionId;
	private String transactionLocation;

}