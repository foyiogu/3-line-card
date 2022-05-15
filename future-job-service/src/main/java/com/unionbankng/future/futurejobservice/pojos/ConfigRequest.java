package com.unionbankng.future.futurejobservice.pojos;

import com.unionbankng.future.futurejobservice.enums.ConfigReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ConfigRequest {
    protected ConfigReference name;
    protected String value;
}