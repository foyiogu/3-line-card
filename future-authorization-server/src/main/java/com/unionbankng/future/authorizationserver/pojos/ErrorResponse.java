
package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorResponse {
    private String code;
    private String remark;
}
