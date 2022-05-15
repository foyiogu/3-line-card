package com.unionbankng.future.futuremessagingservice.pojos;

import lombok.Data;
public @Data class SendGridEmailBody {

	private SendGridEmailPersonalization personalizations;
	private SendGridEmailContent content;
	private SendGridEmailPerson from;
	private SendGridEmailPerson reply_to;
	

}
