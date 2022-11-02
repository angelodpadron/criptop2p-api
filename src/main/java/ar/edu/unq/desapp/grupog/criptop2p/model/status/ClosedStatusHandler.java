package ar.edu.unq.desapp.grupog.criptop2p.model.status;

import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionClosedException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionStatus;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;

public class ClosedStatusHandler extends TransactionStatusHandler {

    @Override
    public boolean canHandleStatus(TransactionStatus status) {
        return status == TransactionStatus.CLOSED;
    }

    @Override
    public void cancelTransaction(TransactionOrder transactionOrder, User user) throws TransactionOrderException {
        throwClosedStatusException();
    }

    @Override
    public void notifyTransferenceFor(TransactionOrder transactionOrder, User userWhoNotifiesTransference) throws TransactionOrderException {
        throwClosedStatusException();
    }

    @Override
    public void notifyReceptionFor(TransactionOrder transactionOrder, User userWhoNotifiesReception) throws TransactionOrderException {
        throwClosedStatusException();
    }

    private void throwClosedStatusException() throws TransactionOrderException {
        throw new TransactionClosedException("The transaction has been closed and cannot be processed");
    }
}
