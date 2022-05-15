package com.unionbankng.future.authorizationserver.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class KycApiResponse<T> {

	private String message;
	private String code;
	private boolean success;
	private T data;

}
