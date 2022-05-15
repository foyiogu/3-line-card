package com.unionbankng.future.authorizationserver.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KYCTokenResponse {

	private String access_token ;
	private String token_type ;
	private String refresh_token ;
	private String expires_in ;
	private String scope ;


}
