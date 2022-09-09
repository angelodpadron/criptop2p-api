package ar.edu.unq.desapp.grupog.criptop2p.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @JsonProperty("first_name")
    @Size(min = 3, max = 30, message = "Invalid name format. The length must be from 3 to 30 characters.")
    private String firstname;
    @JsonProperty("last_name")
    @Size(min = 3, max = 30, message = "Invalid last name format. The length must be from 3 to 30 characters.")
    private String lastname;
    @JsonProperty("email")
    @Email(message = "Invalid email format.")
    private String email;
    @JsonProperty("password")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "The password must contain at least an uppercase, a lowercase, a number and a special character")
    private String password;
    @JsonProperty("address")
    @Size(min = 10, max = 30)
    private String address;
    @JsonProperty("cvu_mercadopago")
    @Pattern(regexp = "^\\d{22}$", message = "Invalid CVU format. The CVU consists of a 22 digit number.")
    private String cvuMercadoPago;
    @JsonProperty("wallet_address")
    @Pattern(regexp = "^\\d{8}$", message = "Invalid wallet address format. The wallet address consists of a 8 digit number.")
    private String walletAddress;

    public User(String firstname, String lastname, String email, String password, String address, String cvuMercadoPago, String walletAddress) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.cvuMercadoPago = cvuMercadoPago;
        this.walletAddress = walletAddress;
    }

    public User() {

    }

    @Override
    public String toString() {
        return "User{" + "id=" + this.id + '\'' + "email=" + this.email + '\'' + '}';
    }
}
