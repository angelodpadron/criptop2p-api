package ar.edu.unq.desapp.grupog.criptop2p.model.status;

import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionCancelledException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionStatus;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;

public class CancelledStatusHandler extends TransactionStatusHandler {

    @Override
    public boolean canHandleStatus(TransactionStatus status) {
        return status == TransactionStatus.CANCELLED || status == TransactionStatus.CANCELLED_BY_SYSTEM;
    }

    @Override
    public void cancelTransaction(TransactionOrder transactionOrder, User user) throws TransactionOrderException {
        throwCancelledTransactionException();
    }

    @Override
    public void notifyTransferenceFor(TransactionOrder transactionOrder, User userWhoNotifiesTransference) throws TransactionOrderException {
        throwCancelledTransactionException();
    }

    @Override
    public void notifyReceptionFor(TransactionOrder transactionOrder, User userWhoNotifiesReception) throws TransactionOrderException {
        throwCancelledTransactionException();
    }

    private void throwCancelledTransactionException() throws TransactionOrderException {
        throw new TransactionCancelledException("The transaction has been cancelled and cannot be processed");

    }
}
