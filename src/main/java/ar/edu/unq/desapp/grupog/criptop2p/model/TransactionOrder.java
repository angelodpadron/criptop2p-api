package ar.edu.unq.desapp.grupog.criptop2p.model;

import ar.edu.unq.desapp.grupog.criptop2p.exception.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.TransactionStatusException;
import ar.edu.unq.desapp.grupog.criptop2p.model.status.TransactionStatusHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    private TransactionStatus transactionStatus = TransactionStatus.AWAITING_TRANSFERENCE;

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

    public void cancelTransactionFor(User user) throws TransactionOrderException, TransactionStatusException {
        TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionStatus)
                .cancelTransaction(this, user);

        marketOrder.setAvailable(true);
    }

    public void performTransferenceAs(User payingUser) throws TransactionStatusException, TransactionOrderException {
        TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionStatus)
                .performTransferenceFor(this, payingUser);
    }

    public void confirmReceptionAs(User confirmingUser) throws TransactionStatusException, TransactionOrderException {
        TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionStatus)
                .confirmReceptionFor(this, confirmingUser);

        if (transactionStatus == TransactionStatus.CLOSED) {
            addOperationAmountToUsers();
            addPointsToUsers(LocalDateTime.now());
        }

    }

    private void addOperationAmountToUsers() {
        dealerUser.setOperations(dealerUser.getOperations() + 1);
        interestedUser.setOperations(interestedUser.getOperations() + 1);
    }

    private void addPointsToUsers(LocalDateTime closingTime) {
        int timeRequiredForClosing = (int) ChronoUnit.MINUTES.between(creationDate, closingTime);
        int pointsToBeAdded = 5;

        if (timeRequiredForClosing < 30) {
            pointsToBeAdded = 10;
        }

        dealerUser.addPoints(pointsToBeAdded);
        interestedUser.addPoints(pointsToBeAdded);
    }
}
