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
    public void performTransferenceFor(TransactionOrder transactionOrder, User payingUser) throws TransactionOrderException {
        throwClosedStatusException();
    }

    @Override
    public void confirmReceptionFor(TransactionOrder transactionOrder, User confirmingUser) throws TransactionOrderException {
        throwClosedStatusException();
    }

    private void throwClosedStatusException() throws TransactionOrderException {
        throw new TransactionClosedException("The transaction has been closed and cannot be processed");
    }
}
