package ar.edu.unq.desapp.grupog.criptop2p.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TRANSACTION_ORDERS")
public class TransactionOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private MarketOrder marketOrder;
    private LocalDateTime creationDate;
    @OneToOne
    private User orderCreator;
    @OneToOne
    private User interestedUser;
    @Enumerated(EnumType.ORDINAL)
    private TransactionStatus status;

}
