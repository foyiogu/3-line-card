package com.unionbankng.future.futurejobservice.pojos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class TransferBody {

    private int amount;
    private String creditAccountName;
    private String creditAccountBankCode;
    private String creditAccountNumber;
    private String creditNarration;
    private String creditAccountBranchCode;
    private String debitAccountBranchCode;
    private String creditAccountType;
    private String currency;
    private String debitAccountName;
    private String debitAccountNumber;
    private String debitNarration;
    private String debitAccountType;
    private String initBranchCode;
    private  String paymentReference;
}
