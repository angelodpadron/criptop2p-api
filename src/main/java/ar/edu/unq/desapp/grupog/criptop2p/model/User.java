package ar.edu.unq.desapp.grupog.criptop2p.model;

import ar.edu.unq.desapp.grupog.criptop2p.exception.MarketOrderException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    public void addMarketOrder(MarketOrder marketOrder) {
        marketOrders.add(marketOrder);
    }

    public void addTransactionOrder(TransactionOrder transactionOrder) {
        transactionOrders.add(transactionOrder);
    }

    public TransactionOrder applyTo(MarketOrder marketOrder) throws MarketOrderException {
        TransactionOrder transactionOrder = marketOrder.generateTransaction(this);
        return transactionOrder;
    }

    public void cancelTransactionOrder(TransactionOrder transactionOrder) {
        transactionOrder.cancelTransactionFor(this);
    }

    public void substratePoints(int points) {
        this.points -= points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}
