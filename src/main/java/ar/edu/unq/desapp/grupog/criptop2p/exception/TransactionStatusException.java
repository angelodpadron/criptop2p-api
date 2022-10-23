package ar.edu.unq.desapp.grupog.criptop2p.exception;

public class TransactionStatusException extends Exception {
    public TransactionStatusException(String status) {
        super(status);
    }
}
