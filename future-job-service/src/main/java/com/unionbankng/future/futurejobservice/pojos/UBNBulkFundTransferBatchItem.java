package com.unionbankng.future.futurejobservice.pojos;


import lombok.Data;

@Data
public class UBNBulkFundTransferBatchItem {
	private String transactionId;
	private String	accountNumber;
	private String	accountType;
	private String	accountName;
	private String	accountBranchCode;
	private String  accountBankCode;
	private String	narration;
	private String	instrumentNumber;
	private String	amount;
	private String	valueDate;
	private String	crDrFlag;
	private String	feeOrCharges;
}