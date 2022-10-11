package ar.edu.unq.desapp.grupog.criptop2p.model;

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
    @OneToMany
    private List<MarketOrder> marketOrders = new ArrayList<>();
    @JsonIgnore
    @OneToMany
    private List<TransactionOrder> transactionOrders = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();
    private Integer points = 0;
    private Integer operations = 0;

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
}
