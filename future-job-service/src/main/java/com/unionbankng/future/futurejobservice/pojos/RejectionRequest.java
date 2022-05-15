package com.unionbankng.future.futurejobservice.pojos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class RejectionRequest {
    protected String reason;
}