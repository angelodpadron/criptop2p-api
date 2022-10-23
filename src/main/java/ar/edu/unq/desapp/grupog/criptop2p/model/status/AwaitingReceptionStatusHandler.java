package ar.edu.unq.desapp.grupog.criptop2p.model.status;

import ar.edu.unq.desapp.grupog.criptop2p.exception.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.OperationType;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionStatus;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;

public class AwaitingReceptionStatusHandler extends TransactionStatusHandler {

    @Override
    public boolean canHandleStatus(TransactionStatus status) {
        return status == TransactionStatus.AWAITING_RECEPTION;
    }

    //    @Override
    public void handleTransaction(TransactionOrder transactionOrder, User user) throws TransactionOrderException {
        checkIfUserCanOperateTransaction(transactionOrder, user);

        OperationType operation = transactionOrder.getMarketOrder().getOperation();

        if (operation == OperationType.PURCHASE) {
            // the interested user is the one who has to confirm reception
            if (user.equals(transactionOrder.getInterestedUser())) {
                transactionOrder.setTransactionStatus(TransactionStatus.CLOSED);
                transactionOrder.setTransactionStatus(transactionOrder.getTransactionStatus());

            } else {
                throw new TransactionOrderException("Only the interested part can confirm the reception of a purchase operation");
            }
        }

        if (operation == OperationType.SELL) {
            // the dealer user is the one who has to confirm reception
            if (user.equals(transactionOrder.getDealerUser())) {
                transactionOrder.setTransactionStatus(TransactionStatus.CLOSED);
                transactionOrder.setTransactionStatus(transactionOrder.getTransactionStatus());
            } else {
                throw new TransactionOrderException("Only the dealer part can confirm the reception of a sell operation");
            }
        }
    }

    @Override
    public void confirmReceptionFor(TransactionOrder transactionOrder, User confirmingUser) throws TransactionOrderException {
        handleTransaction(transactionOrder, confirmingUser);
    }

    @Override
    public void performTransferenceFor(TransactionOrder transactionOrder, User payingUser) throws TransactionOrderException {
        throw new TransactionOrderException("The transfer has already been made");
    }

}
