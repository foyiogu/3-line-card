package com.unionbankng.future.futurejobservice.pojos;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long id;
    private String img;
    private String fullName;
    private String uuid;
    private String umid;
    private String username;
    private String email;
    private String phoneNumber;
    private String accountNumber;
    private String accountName;
    private String address;
    private String country;
    private String stateOfResidence;
    private Boolean isEnabled;
    private String createdAt;
}
