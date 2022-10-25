package ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder;

public class TransferAlreadyMadeException extends TransactionOrderException {
    public TransferAlreadyMadeException(String message) {
        super(message);
    }
}
