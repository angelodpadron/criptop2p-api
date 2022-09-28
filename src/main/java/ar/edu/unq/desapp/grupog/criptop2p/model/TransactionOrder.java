package ar.edu.unq.desapp.grupog.criptop2p.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    private TransactionStatus status = TransactionStatus.PENDING;

    public void closeTransaction() {
        status = TransactionStatus.CLOSED;
    }

    public void cancelTransaction() {
        status = TransactionStatus.CANCELED;
    }


}
