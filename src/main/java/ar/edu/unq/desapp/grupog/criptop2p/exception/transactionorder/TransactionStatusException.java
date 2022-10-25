package ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder;

public class TransactionStatusException extends Exception {
    public TransactionStatusException(String status) {
        super(status);
    }
}
