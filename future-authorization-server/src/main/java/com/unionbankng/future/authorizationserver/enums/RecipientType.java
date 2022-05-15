package com.unionbankng.future.authorizationserver.enums;

public enum RecipientType {

    TO,
    CC,
    BCC;

    public String value() {
        return name();
    }

    public static RecipientType fromValue(String v) {
        return valueOf(v);
    }

}