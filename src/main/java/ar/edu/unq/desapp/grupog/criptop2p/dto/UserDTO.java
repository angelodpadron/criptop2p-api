package ar.edu.unq.desapp.grupog.criptop2p.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDTO {
    @Size(min = 3, max = 30, message = "Invalid name format. The length must be from 3 to 30 characters.")
    @NotEmpty
    private String firstname;
    @Size(min = 3, max = 30, message = "Invalid last name format. The length must be from 3 to 30 characters.")
    @NotEmpty
    private String lastname;
    @Email(message = "Invalid email format.")
    @NotEmpty
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "The password must contain at least an uppercase, a lowercase, a number and a special character")
    @NotEmpty
    private String password;
    @Size(min = 10, max = 30)
    @NotEmpty
    private String address;
    @Pattern(regexp = "^\\d{22}$", message = "Invalid CVU format. The CVU consists of a 22 digit number.")
    @NotEmpty
    private String cvuMercadoPago;
    @Pattern(regexp = "^\\d{8}$", message = "Invalid wallet address format. The wallet address consists of a 8 digit number.")
    @NotEmpty
    private String walletAddress;

}
