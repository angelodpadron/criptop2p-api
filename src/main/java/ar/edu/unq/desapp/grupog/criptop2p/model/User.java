package ar.edu.unq.desapp.grupog.criptop2p.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String address;
    private String cvuMercadoPago;
    private String walletAddress;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private List<MarketOrder> marketOrders = new ArrayList<>();
    @JsonIgnore
    @ManyToMany
    private List<TransactionOrder> transactionOrders = new ArrayList<>();
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

    public void addMarketOrder(MarketOrder marketOrder) {
        marketOrders.add(marketOrder);
    }

    public void addTransactionOrder(TransactionOrder transactionOrder) {
        transactionOrders.add(transactionOrder);
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void applyCancellationPenalty() {
        points -= 20;
    }

    public boolean hasSameEmail(User user) {
        return Objects.equals(email, user.getEmail());
    }
}
