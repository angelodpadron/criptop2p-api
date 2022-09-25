package ar.edu.unq.desapp.grupog.criptop2p.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionOrderTest {

    @Test
    @DisplayName("A created transaction order has a PENDING status")
    public void aTransactionWithPendingStatusTest() {
        TransactionOrder transactionOrder = new TransactionOrder();

        assertEquals(TransactionStatus.PENDING, transactionOrder.getStatus());

    }

    @Test
    @DisplayName("A canceled transaction order has a CANCELED status")
    public void aTransactionWithCanceledStatusTest() {
        TransactionOrder transactionOrder = new TransactionOrder();
        transactionOrder.cancelTransaction();

        assertEquals(TransactionStatus.CANCELED, transactionOrder.getStatus());
    }

    @Test
    @DisplayName("A closed transaction order has a CLOSED status")
    public void aTransactionWithClosedStatusTest() {
        TransactionOrder transactionOrder = new TransactionOrder();
        transactionOrder.closeTransaction();

        assertEquals(TransactionStatus.CLOSED, transactionOrder.getStatus());
    }


}
