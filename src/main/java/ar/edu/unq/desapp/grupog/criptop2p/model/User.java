package ar.edu.unq.desapp.grupog.criptop2p.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
    private String cvuMercadoPago;
    @Pattern(regexp = "^\\d{8}$", message = "Invalid wallet address format. The wallet address consists of a 8 digit number.")
    @NotEmpty
    private String walletAddress;
    @OneToMany
    private List<MarketOrder> marketOrders;
    @OneToMany
    private List<TransactionOrder> pendingOrders;


}
