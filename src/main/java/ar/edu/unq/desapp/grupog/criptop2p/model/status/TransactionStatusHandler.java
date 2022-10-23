package ar.edu.unq.desapp.grupog.criptop2p.model.status;

import ar.edu.unq.desapp.grupog.criptop2p.exception.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.TransactionStatusException;
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
                .filter(handler -> handler.canHandleStatus(transactionStatus)).findAny().orElseThrow(() -> new TransactionStatusException("Nonexistent status"));

    }

    public abstract boolean canHandleStatus(TransactionStatus status);

    public abstract void performTransferenceFor(TransactionOrder transactionOrder, User payingUser) throws TransactionOrderException;

    public abstract void confirmReceptionFor(TransactionOrder transactionOrder, User confirmingUser) throws TransactionOrderException;

    public void cancelTransaction(TransactionOrder transactionOrder, User user) throws TransactionOrderException {
        checkIfUserCanOperateTransaction(transactionOrder, user);
        transactionOrder.setTransactionStatus(TransactionStatus.CANCELLED);
        user.applyCancellationPenalty();
    }

    protected void checkIfUserCanOperateTransaction(TransactionOrder transactionOrder, User user) throws TransactionOrderException {
        if (!(user.equals(transactionOrder.getDealerUser()) || user.equals(transactionOrder.getInterestedUser()))) {
            throw new TransactionOrderException("Transaction cannot be operated by unrelated users");
        }
    }
}
