package ar.edu.unq.desapp.grupog.criptop2p.model;

import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static ar.edu.unq.desapp.grupog.criptop2p.ModelTestResources.getSellingTransactionOrder1;
import static ar.edu.unq.desapp.grupog.criptop2p.model.TransactionStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionOrderTest {

    @Test
    @DisplayName("A created transaction order has its status as \"AWAITING_TRANSFERENCE\"")
    void createdTransactionOrderStatusTest() {
        TransactionOrder transactionOrder = new TransactionOrder();
        assertEquals(AWAITING_TRANSFERENCE, transactionOrder.getTransactionStatus());
    }

    @Test
    @DisplayName("When a transaction order is cancelled as system, its status changes to \"CANCELLED_BY_SYSTEM\"")
    void cancellingATransactionOrderBySystemTest() {
        TransactionOrder transactionOrder = new TransactionOrder();
        transactionOrder.cancelTransactionAsSystem();
        assertEquals(CANCELLED_BY_SYSTEM, transactionOrder.getTransactionStatus());

    }

    @Test
    @DisplayName("When a transaction order is cancelled by a user, its status changes to \"CANCELLED\"")
    void cancellingATransactionOrderTest() throws TransactionOrderException, TransactionStatusException {
        TransactionOrder transactionOrder = getSellingTransactionOrder1();
        transactionOrder.cancelTransactionFor(transactionOrder.getDealerUser());
        assertEquals(CANCELLED, transactionOrder.getTransactionStatus());

    }

    @Test
    @DisplayName("When a transference of a selling transaction order is notified by the interested user, its status changes to \"AWAITING_RECEPTION\"")
    void notifyingTransferenceOfATransactionOrderTest() throws TransactionOrderException, TransactionStatusException {
        TransactionOrder transactionOrder = getSellingTransactionOrder1();
        transactionOrder.notifyTransferenceAs(transactionOrder.getInterestedUser());
        assertEquals(AWAITING_RECEPTION, transactionOrder.getTransactionStatus());
    }

    @Test
    @DisplayName("When the reception of a selling transaction order is notified by the dealer user, its status changes to \"CLOSED\"")
    void notifyingReceptionOfATransactionOrderTest() throws TransactionOrderException, TransactionStatusException {
        TransactionOrder transactionOrder = getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(AWAITING_RECEPTION);

        transactionOrder.notifyReceptionAs(transactionOrder.getDealerUser());

        assertEquals(CLOSED, transactionOrder.getTransactionStatus());
    }

    @Test
    @DisplayName("When a transaction order is closed, the operation amount of the party users its increased by 1")
    void increasingOperationAmountToPartyOfTransactionOrderTest() throws TransactionOrderException, TransactionStatusException {
        TransactionOrder transactionOrder = getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(AWAITING_RECEPTION);

        transactionOrder.notifyReceptionAs(transactionOrder.getDealerUser());

        assertEquals(1, transactionOrder.getDealerUser().getOperations());
        assertEquals(1, transactionOrder.getInterestedUser().getOperations());

    }

    @Test
    @DisplayName("When a transaction order is closed within the first 30 minutes of creation, 10 points are added to the party users")
    void addingTenPointsToPartyUsersAfterClosingTransactionOrderTest() throws TransactionOrderException, TransactionStatusException {
        TransactionOrder transactionOrder = getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(AWAITING_RECEPTION);

        transactionOrder.notifyReceptionAs(transactionOrder.getDealerUser());

        assertEquals(10, transactionOrder.getDealerUser().getPoints());
        assertEquals(10, transactionOrder.getInterestedUser().getPoints());

    }

    @Test
    @DisplayName("When a transaction order is closed after 30 minutes of creation, 5 points are added to the party users")
    void addingFivePointsToPartyUsersAfterClosingTransactionOrderTest() throws TransactionOrderException, TransactionStatusException {
        TransactionOrder transactionOrder = getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(AWAITING_RECEPTION);
        transactionOrder.setCreationDate(LocalDateTime.now().minusMinutes(30));

        transactionOrder.notifyReceptionAs(transactionOrder.getDealerUser());

        assertEquals(5, transactionOrder.getDealerUser().getPoints());
        assertEquals(5, transactionOrder.getInterestedUser().getPoints());

    }


}
