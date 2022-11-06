package ar.edu.unq.desapp.grupog.criptop2p.exception.cryptoquotation;

public class SymbolNotFoundException extends CryptoQuotationException {
    public SymbolNotFoundException(String symbol) {
        super(symbol + " is not a valid cryptocurrency symbol");
    }
}
