package com.unionbankng.future.paymentservice.pojos;
import lombok.Data;

import java.util.List;

@Data
public class InterswitchBankResponse {
	private List<InterswitchBank> banks;
}