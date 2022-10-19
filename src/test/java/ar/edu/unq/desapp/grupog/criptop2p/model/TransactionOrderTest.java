package ar.edu.unq.desapp.grupog.criptop2p.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionOrderTest {

    @Test
    @DisplayName("A created transaction order has a AWAITING_PAYMENT status")
    public void aTransactionWithAwaitingPaymentStatusTest() {
        TransactionOrder transactionOrder = new TransactionOrder();

        assertEquals(TransactionStatus.AWAITING_PAYMENT, transactionOrder.getStatus());

    }

    @Test
    @DisplayName("A payed transaction has a AWAITING_RECEPTION status")
    public void aTransactionWithAwaitingReceptionStatusTest() {
        TransactionOrder transactionOrder = new TransactionOrder();
        transactionOrder.payTransaction();

        assertEquals(TransactionStatus.AWAITING_RECEPTION, transactionOrder.getStatus());
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


    @Test
    @DisplayName("A transaction can be generated and saved on both parties from a market order")
    public void aTransactionGeneratedWithAMarketOrderTest() {
        MarketOrder marketOrder = mock(MarketOrder.class);
        User interestedUser = new User();
        User dealerUser = new User();

        when(marketOrder.getCreator()).thenReturn(dealerUser);

        TransactionOrder transactionOrder = TransactionOrder.generateFor(marketOrder, interestedUser);

        assertTrue(dealerUser.getTransactionOrders().contains(transactionOrder));
        assertTrue(interestedUser.getTransactionOrders().contains(transactionOrder));

    }

}
