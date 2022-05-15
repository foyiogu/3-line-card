package com.unionbankng.future.futurejobservice.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UbnEmailResponse {
	private String message ;
	private String code ;
	private String reference;

}