package ar.edu.unq.desapp.grupog.criptop2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestBody {
    @Size(min = 3, max = 30, message = "Invalid name format. The length must be from 3 to 30 characters.")
    @NotEmpty
    private String firstname;
    @Size(min = 3, max = 30, message = "Invalid last name format. The length must be from 3 to 30 characters.")
    @NotEmpty
    private String lastname;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format.")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "The password must contain at least an uppercase, a lowercase, a number and a special character")
    @NotEmpty
    private String password;
    @Size(min = 10, max = 30)
    @NotEmpty
    private String address;
    @Pattern(regexp = "^\\d{22}$", message = "Invalid CVU format. The CVU consists of a 22 digit number.")
    @NotEmpty
    @JsonProperty("cvu_mercadopago")
    private String cvuMercadoPago;
    @Pattern(regexp = "^\\d{8}$", message = "Invalid wallet address format. The wallet address consists of a 8 digit number.")
    @NotEmpty
    @JsonProperty("wallet_address")
    private String walletAddress;

}
