package com.unionbankng.future.futurejobservice.pojos;
import lombok.Data;

@Data
public class PaymentResponse {
	private String code;
	private String message;
	private String reference;
	private String batchId;
	private String cbaBatchNo;
}