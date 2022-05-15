package com.unionbankng.future.futurejobservice.enums;

public enum ConfigReference {

    VAT_RATE,
    KULA_INCOME,
    UBN_INCOME,
    TOTAL_JOBS,
    TOTAL_JOBS_COMPLETED,
    TOTAL_JOBS_REJECTED,
    PEPPEREST_INCOME_ACCOUNT_NAME,
    PEPPEREST_INCOME_ACCOUNT_NUMBER,
    KULA_INCOME_ACCOUNT_NAME,
    KULA_INCOME_ACCOUNT_NUMBER,
    VAT_INCOME_ACCOUNT_NAME,
    VAT_INCOME_ACCOUNT_NUMBER,
    ESCROW_ACCOUNT_NAME,
    ESCROW_ACCOUNT_NUMBER;
    public String value() {
        return name();
    }

    public static ConfigReference fromValue(String v) {
        return valueOf(v);
    }

}