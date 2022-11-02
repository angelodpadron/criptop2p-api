package ar.edu.unq.desapp.grupog.criptop2p.model;

public enum OperationType {
    SELL {
        @Override
        boolean priceIsValid(Double currentQuotation, Double targetPrice) {
            return currentQuotation <= targetPrice;
        }
    },
    PURCHASE {
        @Override
        boolean priceIsValid(Double currentQuotation, Double targetPrice) {
            return currentQuotation >= targetPrice;
        }
    };

    abstract boolean priceIsValid(Double currentQuotation, Double targetPrice);
}
