package com.unionbankng.future.paymentservice.pojos;

import lombok.Data;

@Data
public class PaystackTransaction {
	private String event;
	private PaystackData data;
}