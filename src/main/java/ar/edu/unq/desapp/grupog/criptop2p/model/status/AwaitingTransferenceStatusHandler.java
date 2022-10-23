package ar.edu.unq.desapp.grupog.criptop2p.model.status;

import ar.edu.unq.desapp.grupog.criptop2p.exception.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.OperationType;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionStatus;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;

public class AwaitingTransferenceStatusHandler extends TransactionStatusHandler {

    @Override
    public boolean canHandleStatus(TransactionStatus status) {
        return status == TransactionStatus.AWAITING_TRANSFERENCE;
    }

    //    @Override
    public void handleTransaction(TransactionOrder transactionOrder, User user) throws TransactionOrderException {
        checkIfUserCanOperateTransaction(transactionOrder, user);

        OperationType operation = transactionOrder.getMarketOrder().getOperation();

        if (operation == OperationType.PURCHASE) {
            // only the dealer user can perform the transference
            if (user.equals(transactionOrder.getDealerUser())) {
                transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_RECEPTION);
                transactionOrder.setTransactionStatus(transactionOrder.getTransactionStatus());
            } else {
                throw new TransactionOrderException("Only the dealer user can perform a transference of a purchase operation");
            }
        }

        if (operation == OperationType.SELL) {
            // only the interested user can perform the transference
            if (user.equals(transactionOrder.getInterestedUser())) {
                transactionOrder.setTransactionStatus(TransactionStatus.AWAITING_RECEPTION);
                transactionOrder.setTransactionStatus(transactionOrder.getTransactionStatus());
            } else {
                throw new TransactionOrderException("Only the interested user can perform a transference of a sell operation");
            }
        }
    }

    @Override
    public void performTransferenceFor(TransactionOrder transactionOrder, User payingUser) throws TransactionOrderException {
        handleTransaction(transactionOrder, payingUser);
    }

    @Override
    public void confirmReceptionFor(TransactionOrder transactionOrder, User confirmingUser) throws TransactionOrderException {
        throw new TransactionOrderException("Reception cannot be confirmed without prior transference");
    }


}
