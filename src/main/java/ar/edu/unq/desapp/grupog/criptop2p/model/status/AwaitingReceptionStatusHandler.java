package ar.edu.unq.desapp.grupog.criptop2p.model.status;

import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.IllegalTransactionOperationException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransferAlreadyMadeException;
import ar.edu.unq.desapp.grupog.criptop2p.model.OperationType;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionStatus;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;

public class AwaitingReceptionStatusHandler extends TransactionStatusHandler {

    @Override
    public boolean canHandleStatus(TransactionStatus status) {
        return status == TransactionStatus.AWAITING_RECEPTION;
    }

    private void handleTransaction(TransactionOrder transactionOrder, User user) throws TransactionOrderException {
        checkIfUserCanOperateTransaction(transactionOrder, user);

        OperationType operation = transactionOrder.getMarketOrder().getOperation();

        if (operation == OperationType.PURCHASE) {
            // the interested user is the one who has to confirm reception
            if (user.hasSameEmail(transactionOrder.getInterestedUser())) {
                transactionOrder.setTransactionStatus(TransactionStatus.CLOSED);
                transactionOrder.setTransactionStatus(transactionOrder.getTransactionStatus());

            } else {
                throw new IllegalTransactionOperationException("Only the interested part can confirm the reception of a purchase operation");
            }
        }

        if (operation == OperationType.SELL) {
            // the dealer user is the one who has to confirm reception
            if (user.hasSameEmail(transactionOrder.getDealerUser())) {
                transactionOrder.setTransactionStatus(TransactionStatus.CLOSED);
                transactionOrder.setTransactionStatus(transactionOrder.getTransactionStatus());
            } else {
                throw new IllegalTransactionOperationException("Only the dealer part can confirm the reception of a sell operation");
            }
        }
    }

    @Override
    public void notifyReceptionFor(TransactionOrder transactionOrder, User userWhoNotifiesReception) throws TransactionOrderException {
        handleTransaction(transactionOrder, userWhoNotifiesReception);
    }

    @Override
    public void notifyTransferenceFor(TransactionOrder transactionOrder, User userWhoNotifiesTransference) throws TransactionOrderException {
        throw new TransferAlreadyMadeException("The transfer has already been made");
    }

}
