package com.unionbankng.future.paymentservice.pojos;

import lombok.Data;

public @Data class ApiResponse<T> {

	private String message;
	private String code;
	private boolean success;
	private T payload;


	public ApiResponse(String message, boolean success, String code, T payload) {
		super();
		this.message = message;
		this.success = success;
		this.code = code;
		this.payload = payload;
	}

	public ApiResponse(String message, boolean success, T payload) {
		super();
		this.message = message;
		this.success = success;
		this.payload = payload;
	}

     
}
