package com.unionbankng.future.authorizationserver.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class APIResponse<T> {
    private String message;
    private boolean success;
    private T payload;
}
