package ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder;

public class MarketOrderAlreadyTakenException extends TransactionOrderException {
    public MarketOrderAlreadyTakenException(String message) {
        super(message);
    }
}
