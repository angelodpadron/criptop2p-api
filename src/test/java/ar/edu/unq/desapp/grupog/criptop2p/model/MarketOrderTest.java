package ar.edu.unq.desapp.grupog.criptop2p.model;

import ar.edu.unq.desapp.grupog.criptop2p.exception.InvalidOperationPriceException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.MarketOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MarketOrderTest {

    @Test
    @DisplayName("A target price can vary five percent the market price")
    public void aMarketOrderWithATargetPriceOfFivePercentLimitTest() throws MarketOrderException {
        Double marketPrice = 100.0;
        Double targetPrice = marketPrice + marketPrice * 0.5;

        try {
            generateMarketOrderWithPrices(targetPrice, marketPrice);
        } catch (InvalidOperationPriceException exception) {
            fail("Should not throw exception", exception);
        }
    }

    @Test
    @DisplayName("A target price cannot be greater than five percent the market price")
    public void aMarketOrderWithATargetPriceGreaterThanFivePercentLimitTest() {
        Double marketPrice = 100.0;
        Double marketPricePlusFivePercent = marketPrice + marketPrice * 0.5;
        Double targetPrice = marketPricePlusFivePercent + 1;

        Exception exception = assertThrows(InvalidOperationPriceException.class, () -> generateMarketOrderWithPrices(targetPrice, marketPrice));

        assertEquals("Target price exceeds allowable variation", exception.getMessage());
    }

    @Test
    @DisplayName("A target price cannot be lesser than five percent the market price")
    public void aMarketOrderWithATargetPriceLesserThanFivePercentLimitTest() {
        Double marketPrice = 100.0;
        Double marketPriceLessFivePercent = marketPrice - marketPrice * 0.5;
        Double targetPrice = marketPriceLessFivePercent - 1;

        Exception exception = assertThrows(InvalidOperationPriceException.class, () -> generateMarketOrderWithPrices(targetPrice, marketPrice));

        assertEquals("Target price exceeds allowable variation", exception.getMessage());

    }


    private MarketOrder generateMarketOrderWithPrices(Double targetPrice, Double marketPrice) throws MarketOrderException {
        return new MarketOrder(null, null, null, null, marketPrice, targetPrice, null, null);
    }

}
