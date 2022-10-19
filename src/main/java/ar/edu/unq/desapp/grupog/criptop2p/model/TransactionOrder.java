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
    @ManyToOne
    private MarketOrder marketOrder;
    private LocalDateTime creationDate;
    @ManyToOne
    private User interestedUser;
    @ManyToOne
    private User dealerUser;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.AWAITING_PAYMENT;

    public static TransactionOrder generateFor(MarketOrder marketOrder, User interestedUser) {
        TransactionOrder transactionOrder = new TransactionOrder();
        transactionOrder.setMarketOrder(marketOrder);
        transactionOrder.setCreationDate(LocalDateTime.now());
        transactionOrder.setDealerUser(marketOrder.getCreator());
        transactionOrder.setInterestedUser(interestedUser);

        interestedUser.addTransactionOrder(transactionOrder);
        marketOrder.getCreator().addTransactionOrder(transactionOrder);

        return transactionOrder;
    }

    public void closeTransaction() {
        status = TransactionStatus.CLOSED;
    }

    public void cancelTransaction() {
        status = TransactionStatus.CANCELED;
    }

    public void payTransaction() {
        status = TransactionStatus.AWAITING_RECEPTION;
    }

    public void cancelTransactionFor(User user) {
        user.substratePoints(20);
        cancelTransaction();
        marketOrder.setAvailable(true);
    }
}
