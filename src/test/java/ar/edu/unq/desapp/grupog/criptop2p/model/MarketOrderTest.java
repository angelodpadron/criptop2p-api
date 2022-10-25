package ar.edu.unq.desapp.grupog.criptop2p.model;

import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.InvalidMarketPriceException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.resources.ModelTestResources;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MarketOrderTest {

    @Test
    @DisplayName("A target price can vary five percent the market price")
    public void aMarketOrderWithATargetPriceOfFivePercentLimitTest() throws MarketOrderException {
        Double marketPrice = 100.0;
        Double targetPrice = marketPrice + marketPrice * 0.5;

        try {
            generateMarketOrderWithPrices(targetPrice, marketPrice);
        } catch (InvalidMarketPriceException exception) {
            fail("Should not throw exception", exception);
        }
    }

    @Test
    @DisplayName("A target price cannot be greater than five percent the market price")
    public void aMarketOrderWithATargetPriceGreaterThanFivePercentLimitTest() {
        Double marketPrice = 100.0;
        Double marketPricePlusFivePercent = marketPrice + marketPrice * 0.5;
        Double targetPrice = marketPricePlusFivePercent + 1;

        Exception exception = assertThrows(InvalidMarketPriceException.class, () -> generateMarketOrderWithPrices(targetPrice, marketPrice));

        assertEquals("Target price exceeds allowable variation", exception.getMessage());
    }

    @Test
    @DisplayName("A target price cannot be lesser than five percent the market price")
    public void aMarketOrderWithATargetPriceLesserThanFivePercentLimitTest() {
        Double marketPrice = 100.0;
        Double marketPriceLessFivePercent = marketPrice - marketPrice * 0.5;
        Double targetPrice = marketPriceLessFivePercent - 1;

        Exception exception = assertThrows(InvalidMarketPriceException.class, () -> generateMarketOrderWithPrices(targetPrice, marketPrice));

        assertEquals("Target price exceeds allowable variation", exception.getMessage());

    }

    @Test
    @DisplayName("A market order is initially available")
    public void aMarketOrderAnAvailableStatusTest() {
        MarketOrder marketOrder = new MarketOrder();
        assertTrue(marketOrder.getAvailable());
    }

    @Test
    @DisplayName("A market order is unavailable when a user applies to it")
    public void aMarketOrderGetsUnavailableTest() throws MarketOrderException {
        MarketOrder marketOrder = ModelTestResources.getMarketOrder1();
        User interestedUser = mock(User.class);
        when(interestedUser.getEmail()).thenReturn("interested@email.com");

        marketOrder.generateTransaction(interestedUser);

        assertFalse(marketOrder.getAvailable());
    }

    @Test
    @DisplayName("A market order generates a transaction order when a user apply to it")
    public void aMarketOrderGeneratesATransactionOrderTest() throws MarketOrderException {
        MarketOrder marketOrder = ModelTestResources.getMarketOrder1();
        User interestedUser = ModelTestResources.getBasicUser1();
        User dealerUser = ModelTestResources.getBasicUser2();

        marketOrder.setCreator(dealerUser);
        marketOrder.generateTransaction(interestedUser);

        assertFalse(dealerUser.getTransactionOrders().isEmpty());
        assertFalse(interestedUser.getTransactionOrders().isEmpty());

    }

    @Test
    @DisplayName("A market order cannot generate a transaction for the user who created it")
    public void aMarketOrderCannotGenerateATransactionForTheSameUserTest() {
        MarketOrder marketOrder = ModelTestResources.getMarketOrder1();
        User dealerUser = ModelTestResources.getBasicUser1();
        marketOrder.setCreator(dealerUser);

        assertThrows(MarketOrderException.class, () -> marketOrder.generateTransaction(dealerUser));

    }

    private MarketOrder generateMarketOrderWithPrices(Double targetPrice, Double marketPrice) throws MarketOrderException {
        return new MarketOrder(null, null, null, null, marketPrice, targetPrice, null, null);
    }

}
