package com.unionbankng.future.authorizationserver.pojos;


import lombok.Data;

@Data
public class UBNAuthServerTokenResponse {

	private String access_token ;
	private String token_type ;
	private String refresh_token ;
	private String expires_in ;
	private String scope ;


}