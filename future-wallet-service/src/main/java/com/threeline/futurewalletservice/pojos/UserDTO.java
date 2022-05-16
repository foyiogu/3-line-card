package com.threeline.futurewalletservice.pojos;
import com.threeline.futurewalletservice.enums.Role;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String uuid;
    private String email;
    private String phoneNumber;
    private String walletId;
    private String walletAccountNumber;
    private int kycLevel;
    private Role role;


    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}


