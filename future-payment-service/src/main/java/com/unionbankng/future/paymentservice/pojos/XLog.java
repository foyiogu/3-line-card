package com.unionbankng.future.paymentservice.pojos;

import com.unionbankng.future.paymentservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class XLog implements Serializable{
	
	private Long id;

	private String direction;

	private BigDecimal amount = BigDecimal.ZERO;

	private String currencyCode;

	private String customerName;

	private String walletId;

	private String fromAccountNumber;

	private String fromAccountName;

	private String toAccountNumber;

	private String toAccountName;

	private String narration;

	private String transactionReference;

	private String tradeReference;

	private Date transactionDate;

	private String ftSessionId;

	private String transactionLocation;

	private String nameEnquirySessionId;

	private Date valueDate;

	private BigDecimal balance;

	private Double charge = 0.00;

	private String customerRef ;

	private String transactionType;

	private String nonReversalReason;

    private TransactionType type;

	private Boolean canReverse = true;

	private String externalAccountName;

	private String externalAccountNumber;

	private String toAccountBankName;

	private String toAccountBankCode;

	private String fromAccountBankCode;

	private String fromAccountBankName;

}
