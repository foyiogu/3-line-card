package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

public @Data class ApiResponseProxy<T> {

	private String message;
	private String code;
	private boolean success;
	private T payload;


	public ApiResponseProxy(String message, boolean success, String code, T payload) {
		super();
		this.message = message;
		this.success = success;
		this.code = code;
		this.payload = payload;
	}

     
}
