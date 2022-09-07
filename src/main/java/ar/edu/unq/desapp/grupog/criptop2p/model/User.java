package ar.edu.unq.desapp.grupog.criptop2p.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @JsonProperty("first_name")
    private String firstname;
    @JsonProperty("last_name")

    private String lastname;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("address")
    private String address;
    @JsonProperty("cvu_mercadopago")
    private String cvuMercadoPago;
    @JsonProperty("wallet_address")
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
