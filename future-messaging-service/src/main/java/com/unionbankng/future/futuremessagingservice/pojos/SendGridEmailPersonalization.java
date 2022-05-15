package com.unionbankng.future.futuremessagingservice.pojos;
import lombok.Data;
import java.util.List;

public @Data class SendGridEmailPersonalization {

	private List<SendGridEmailPerson> to;
	private String subject;
}
