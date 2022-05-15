package com.unionbankng.future.futurejobservice.pojos;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UBNBulkFundTransferRequest {
	private String currency;
	private	String initBranchCode;
	private	String paymentReference;
	private	String transactionDate;
	private	String paymentTypeCode;
	private	String externalSystemReference;
	private ArrayList<UBNBulkFundTransferBatchItem> batchItems=new ArrayList<>();

}