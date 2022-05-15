package com.unionbankng.future.futurejobservice.pojos;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtUserDetail {
    private Long userId;
    private String userUUID;
    private String userEmail;
    private String userImg;
    private String userFullName;
    private int kycLevel;
    private String  walletId;
    private Boolean isEnabled;
    private String firstName;
    private String lastName;
    private String userAddress;
    private String city;
    private String gender;
    private String zipCode;
}
