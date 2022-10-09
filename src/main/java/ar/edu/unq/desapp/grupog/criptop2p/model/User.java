package ar.edu.unq.desapp.grupog.criptop2p.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
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
    //    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "The password must contain at least an uppercase, a lowercase, a number and a special character")
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

    @JsonIgnore
    @OneToMany
    private List<MarketOrder> marketOrders;
    @JsonIgnore
    @OneToMany
    private List<TransactionOrder> pendingOrders;
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();
    private Integer points = 0;
    private Integer operations = 0;

    public User(String firstname, String lastname, String email, String password, String address, String cvuMercadoPago, String walletAddress) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.cvuMercadoPago = cvuMercadoPago;
        this.walletAddress = walletAddress;
    }

    public int getReputation() {
        if (operations == 0) {
            return operations;
        }
        return points / operations;
    }

    public void addRole(Role role) {
        roles.add(role);
    }
}
