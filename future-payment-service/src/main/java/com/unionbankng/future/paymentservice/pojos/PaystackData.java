package com.unionbankng.future.paymentservice.pojos;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaystackData {

		private Long id;
		private BigDecimal amount;	 
	    private String currency;
	    private String transaction_date;
	    private String status;
	    private String reference;
	    private String domain;
	    private String gateway_response;
	    private String message;
	    private String channel;
	    private String ip_address;
	    private String fees;
	    private String paid_at;
	    private PaystackAuth authorization;
	    private PaystackMetaData metadata;
	    

	    public PaystackData() {
	    	
	    }

}
