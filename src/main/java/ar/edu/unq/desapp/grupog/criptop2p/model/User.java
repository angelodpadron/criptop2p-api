package ar.edu.unq.desapp.grupog.criptop2p.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    @NotBlank(message = "Firstname is required")
    @Size(min=3, max = 30)
    private String firstname;

    @Column
    @NotBlank(message = "Lastname is required")
    @Size(min=3, max = 30)
    private String lastname;

    @Column
    @NotBlank(message = "Email is required")
    @Email(message = "Incorrect email")
    private String email;

    @Column
    @NotBlank(message = "1 minuscula, 1 mayúscula, 1 carac especial ")
    @Size(min=6)
    private String password;

    @Column
    @NotBlank(message = "Address is required")
    @Size(min=10, max = 30)
    private String address;

    @Column
    @NotBlank(message = "CVU Mercado Pago is required")
    @Size(min=22, max = 22)
    private String cvuMercadoPago;

    @Column
    @NotBlank(message = "Wallet address is required")
    @Size(min=8, max = 8)
    private String walletAddress;

}
