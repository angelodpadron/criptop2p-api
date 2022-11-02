package ar.edu.unq.desapp.grupog.criptop2p.model.status;

import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.IllegalTransactionOperationException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionStatusException;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionStatus;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import lombok.Data;

import java.util.List;

@Data
public abstract class TransactionStatusHandler {
    public static TransactionStatusHandler getTransactionStatusHandlerFor(TransactionStatus transactionStatus) throws TransactionStatusException {

        List<TransactionStatusHandler> handlerList = List.of(
                new AwaitingTransferenceStatusHandler(),
                new AwaitingReceptionStatusHandler(),
                new CancelledStatusHandler(),
                new ClosedStatusHandler()
        );

        return handlerList
                .stream()
                .filter(handler -> handler.canHandleStatus(transactionStatus))
                .findAny()
                .orElseThrow(() -> new TransactionStatusException("Cannot instantiate a handler for the state \"." + transactionStatus + "\""));

    }

    public abstract boolean canHandleStatus(TransactionStatus status);

    public abstract void notifyTransferenceFor(TransactionOrder transactionOrder, User userWhoNotifiesTransference) throws TransactionOrderException;

    public abstract void notifyReceptionFor(TransactionOrder transactionOrder, User userWhoNotifiesReception) throws TransactionOrderException;

    public void cancelTransaction(TransactionOrder transactionOrder, User user) throws TransactionOrderException {
        checkIfUserCanOperateTransaction(transactionOrder, user);
        transactionOrder.setTransactionStatus(TransactionStatus.CANCELLED);
        user.applyCancellationPenalty();
    }

    protected void checkIfUserCanOperateTransaction(TransactionOrder transactionOrder, User user) throws TransactionOrderException {
        if (!(user.hasSameEmail(transactionOrder.getDealerUser()) || user.hasSameEmail(transactionOrder.getInterestedUser()))) {
            throw new IllegalTransactionOperationException("Transaction cannot be operated by unrelated users");
        }
    }
}
