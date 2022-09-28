package ar.edu.unq.desapp.grupog.criptop2p.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestBody {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String address;
    private String cvuMercadoPago;
    private String walletAddress;

}
