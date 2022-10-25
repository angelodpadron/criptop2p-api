package ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder;

public class TransferIsPendingException extends TransactionOrderException {
    public TransferIsPendingException(String message) {
        super(message);
    }
}
