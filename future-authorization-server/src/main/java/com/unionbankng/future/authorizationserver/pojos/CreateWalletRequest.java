package com.unionbankng.future.authorizationserver.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWalletRequest {

	private String userId;
	private String customerName;
	private String bvn;
}
