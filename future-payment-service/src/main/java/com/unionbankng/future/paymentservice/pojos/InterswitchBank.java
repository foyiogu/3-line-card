package com.unionbankng.future.paymentservice.pojos;
import lombok.Data;

@Data
public class InterswitchBank {
	private String id;
	private String cbnCode;
	private String bankName;
	private String bankCode;
}