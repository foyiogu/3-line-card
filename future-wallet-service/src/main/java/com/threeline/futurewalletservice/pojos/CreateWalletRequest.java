package com.threeline.futurewalletservice.pojos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CreateWalletRequest {

    private Long userId;
    private String userUuid;
    private String email;
    private String accountName;
}
