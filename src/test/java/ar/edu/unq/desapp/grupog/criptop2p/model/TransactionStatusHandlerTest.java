package ar.edu.unq.desapp.grupog.criptop2p.model;

import ar.edu.unq.desapp.grupog.criptop2p.ModelTestResources;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.*;
import ar.edu.unq.desapp.grupog.criptop2p.model.status.AwaitingTransferenceStatusHandler;
import ar.edu.unq.desapp.grupog.criptop2p.model.status.TransactionStatusHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionStatusHandlerTest {
    @DisplayName("The transaction status handler returns a specific handler according to a transaction status")
    @Test
    void transactionStatusHandlerReturnsAHandlerTest() throws TransactionStatusException {
        TransactionStatus awaitingTransferenceStatus = TransactionStatus.AWAITING_TRANSFERENCE;
        Assertions.assertEquals(
                AwaitingTransferenceStatusHandler.class,
                TransactionStatusHandler.getTransactionStatusHandlerFor(awaitingTransferenceStatus).getClass());
    }

    @DisplayName("A transaction status handler throws an exception when an unrelated user tries to operate the transaction")
    @Test
    void transactionStatusThrowsExceptionWhenOperatedByUnrelatedUserTest() {
        TransactionOrder transactionOrder = ModelTestResources.getSellingTransactionOrder1();
        User unrelatedUser = mock(User.class);
        when(unrelatedUser.getEmail()).thenReturn("unrelated@domain.com");

        assertThrows(
                IllegalTransactionOperationException.class,
                () -> new AwaitingTransferenceStatusHandler().notifyTransferenceFor(transactionOrder, unrelatedUser));
    }

    @DisplayName("An AWAITING_TRANSFERENCE transaction status handler sets the transaction order status as CANCELLED when the transaction order is cancelled")
    @Test
    void awaitingTransferenceHandlerChangesTheTransactionStatusToCancelledTest() throws TransactionOrderException, TransactionStatusException {
        TransactionOrder transactionOrder = ModelTestResources.getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_TRANSFERENCE);

        TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                .cancelTransaction(transactionOrder, transactionOrder.getDealerUser());

        assertEquals(TransactionStatus.CANCELLED, transactionOrder.getTransactionStatus());

    }

    @DisplayName("An AWAITING_RECEPTION transaction status handler sets the transaction order status as CANCELLED when the transaction order is cancelled")
    @Test
    void awaitingReceptionStatusHandlerChangesTheTransactionStatusToCancelledTest() throws TransactionOrderException, TransactionStatusException {
        TransactionOrder transactionOrder = ModelTestResources.getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_RECEPTION);

        TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                .cancelTransaction(transactionOrder, transactionOrder.getDealerUser());

        assertEquals(TransactionStatus.CANCELLED, transactionOrder.getTransactionStatus());
    }

    @DisplayName("When trying to operate on a cancelled transaction, an exception is thrown")
    @Test
    void cancelledHandlerStatusThrowsExceptionWhenItIsOperatedTest() {
        TransactionOrder transactionOrder = ModelTestResources.getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.CANCELLED);

        assertThrows(TransactionCancelledException.class,
                () -> TransactionStatusHandler
                        .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                        .cancelTransaction(transactionOrder, transactionOrder.getDealerUser()));

    }

    @DisplayName("When trying to operate on a closed transaction, an exception is thrown.")
    @Test
    void closedStatusHandlerThrowsExceptionWhenItIsOperatedTest() {
        TransactionOrder transactionOrder = ModelTestResources.getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.CLOSED);

        assertThrows(TransactionClosedException.class,
                () -> TransactionStatusHandler
                        .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                        .cancelTransaction(transactionOrder, transactionOrder.getDealerUser()));
    }

    @DisplayName("The transference of a sell operation can be only notified by the interested user.")
    @Test
    void performingTransferenceOfASellOperationTest() throws TransactionStatusException, TransactionOrderException {
        TransactionOrder transactionOrder = ModelTestResources.getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_TRANSFERENCE);

        try {
            TransactionStatusHandler
                    .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                    .notifyTransferenceFor(transactionOrder, transactionOrder.getInterestedUser());
        } catch (IllegalTransactionOperationException exception) {
            fail("Should not throw exception", exception);
        }
    }

    @DisplayName("When the transference of a sell operation is notified by the dealer user, an exception is thrown.")
    @Test
    void performingTransferenceOfASellOperationWithExceptionTest() {
        TransactionOrder transactionOrder = ModelTestResources.getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_TRANSFERENCE);

        assertThrows(IllegalTransactionOperationException.class, () -> TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                .notifyTransferenceFor(transactionOrder, transactionOrder.getDealerUser()));
    }

    @DisplayName("The transference of a purchase operation can be only notified by the dealer user.")
    @Test
    void performingTransferenceOfAPurchaseOperationTest() throws TransactionStatusException, TransactionOrderException {
        TransactionOrder transactionOrder = ModelTestResources.getPurchaseTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_TRANSFERENCE);

        try {
            TransactionStatusHandler
                    .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                    .notifyTransferenceFor(transactionOrder, transactionOrder.getDealerUser());
        } catch (IllegalTransactionOperationException exception) {
            fail("Should not throw exception", exception);
        }
    }

    @DisplayName("When the transference of a purchase operation is notified by the interested user, an exception is thrown.")
    @Test
    void performingTransferenceOfAPurchaseOperationWithExceptionTest() {
        TransactionOrder transactionOrder = ModelTestResources.getPurchaseTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_TRANSFERENCE);

        assertThrows(IllegalTransactionOperationException.class, () -> TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                .notifyTransferenceFor(transactionOrder, transactionOrder.getInterestedUser()));

    }

    @DisplayName("The reception of a sell operation can be only notified by the dealer user.")
    @Test
    void confirmReceptionOfASellOperationTest() {
        TransactionOrder transactionOrder = ModelTestResources.getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_RECEPTION);

        try {
            TransactionStatusHandler
                    .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                    .notifyReceptionFor(transactionOrder, transactionOrder.getDealerUser());
        } catch (TransactionOrderException | TransactionStatusException exception) {
            fail("Should not throw exception", exception);
        }
    }

    @DisplayName("When the reception confirmation of a sell operation is notified by the interested user, an exception is thrown.")
    @Test
    void confirmReceptionOfASellingOperationWithExceptionTest() {
        TransactionOrder transactionOrder = ModelTestResources.getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_RECEPTION);

        assertThrows(IllegalTransactionOperationException.class, () -> TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                .notifyReceptionFor(transactionOrder, transactionOrder.getInterestedUser()));
    }

    @DisplayName("The reception of a purchase operation can only be notified by the interested user")
    @Test
    void confirmReceptionOfAPurchaseOperationTest() {
        TransactionOrder transactionOrder = ModelTestResources.getPurchaseTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_RECEPTION);

        try {
            TransactionStatusHandler
                    .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                    .notifyReceptionFor(transactionOrder, transactionOrder.getInterestedUser());
        } catch (TransactionOrderException | TransactionStatusException exception) {
            fail("Should not throw exception", exception);
        }

    }

    @DisplayName("When the reception of a purchase operation is notified by the dealer user, an exception is thrown.")
    @Test
    void confirmReceptionOfAPurchaseOperationWithExceptionTest() {
        TransactionOrder transactionOrder = ModelTestResources.getPurchaseTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_RECEPTION);

        assertThrows(IllegalTransactionOperationException.class, () -> TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                .notifyReceptionFor(transactionOrder, transactionOrder.getDealerUser()));
    }

    @DisplayName("Cannot notify the reception of a transaction order without prior transference notification")
    @Test
    void confirmReceptionOfATransactionWithPendingTransferExceptionTest() {
        TransactionOrder transactionOrder = ModelTestResources.getPurchaseTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_TRANSFERENCE);

        assertThrows(TransferIsPendingException.class, () -> TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                .notifyReceptionFor(transactionOrder, transactionOrder.getInterestedUser()));
    }

    @DisplayName("Cannot notify a transfer of a transaction order when the transfer notification was already made.")
    @Test
    void performTransferenceOfATransactionWithAnAlreadyMadeTransferenceExceptionTest() {
        TransactionOrder transactionOrder = ModelTestResources.getSellingTransactionOrder1();
        transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_RECEPTION);

        assertThrows(TransferAlreadyMadeException.class, () -> TransactionStatusHandler
                .getTransactionStatusHandlerFor(transactionOrder.getTransactionStatus())
                .notifyTransferenceFor(transactionOrder, transactionOrder.getInterestedUser()));
    }


}



