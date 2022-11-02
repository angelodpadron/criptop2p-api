package ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder;

public class PriceExceedsOperationLimitException extends RuntimeException {
    public PriceExceedsOperationLimitException(String message) {
        super(message);
    }
}
