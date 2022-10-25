package ar.edu.unq.desapp.grupog.criptop2p.model.status;

import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionCancelledException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionStatus;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;

public class CancelledStatusHandler extends TransactionStatusHandler {

    @Override
    public boolean canHandleStatus(TransactionStatus status) {
        return status == TransactionStatus.CANCELLED;
    }

    @Override
    public void cancelTransaction(TransactionOrder transactionOrder, User user) throws TransactionOrderException {
        throwCancelledTransactionException();
    }

    @Override
    public void performTransferenceFor(TransactionOrder transactionOrder, User payingUser) throws TransactionOrderException {
        throwCancelledTransactionException();
    }

    @Override
    public void confirmReceptionFor(TransactionOrder transactionOrder, User confirmingUser) throws TransactionOrderException {
        throwCancelledTransactionException();
    }

    private void throwCancelledTransactionException() throws TransactionOrderException {
        throw new TransactionCancelledException("The transaction has been cancelled and cannot be processed");

    }
}
