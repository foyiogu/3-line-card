package com.unionbankng.future.futurejobservice.pojos;

import lombok.Data;

@Data
public class UBNBulkFundTransferResponse {
	private String code;
	private String message;
	private String reference;
	private String batchId;
	private String cbaBatchNo;
}