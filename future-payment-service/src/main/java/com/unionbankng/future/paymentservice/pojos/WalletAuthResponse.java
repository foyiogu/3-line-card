package com.unionbankng.future.paymentservice.pojos;
import lombok.Data;

@Data
public class WalletAuthResponse {
	private String access_token;
	private String refresh_token;
	private String token_type;
	private String expires_in;
	private String scope;
	private String jti;
}