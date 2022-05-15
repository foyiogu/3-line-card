package com.unionbankng.future.futurejobservice.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class SMS implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    private String recipient;
    private String message;
    
}
