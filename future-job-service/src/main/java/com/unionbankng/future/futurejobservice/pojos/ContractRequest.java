package com.unionbankng.future.futurejobservice.pojos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ContractRequest {
    protected String contractReference;
    protected String narration;
}