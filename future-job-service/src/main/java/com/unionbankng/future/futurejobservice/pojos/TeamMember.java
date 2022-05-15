package com.unionbankng.future.futurejobservice.pojos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class TeamMember {

    private Long id;
    private String img;
    private String fullName;
    private String email;
    private String accountName;
    private String accountNumber;
    private String accountType;
    private String branchCode;
    private String percentage;
    private String createdAt;
}
