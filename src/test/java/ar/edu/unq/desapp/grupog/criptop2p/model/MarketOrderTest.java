package ar.edu.unq.desapp.grupog.criptop2p.model;

import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.InvalidMarketPriceException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.PriceExceedsOperationLimitException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.MarketOrderAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static ar.edu.unq.desapp.grupog.criptop2p.ModelTestResources.getPurchaseMarketOrder1;
import static ar.edu.unq.desapp.grupog.criptop2p.ModelTestResources.getSellingMarketOrder1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MarketOrderTest {

    @Test
    @DisplayName("A target price can vary five percent the market price")
    void aMarketOrderWithATargetPriceOfFivePercentLimitTest() throws MarketOrderException {
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
    void aMarketOrderWithATargetPriceGreaterThanFivePercentLimitTest() {
        Double marketPrice = 100.0;
        Double marketPricePlusFivePercent = marketPrice + marketPrice * 0.5;
        Double targetPrice = marketPricePlusFivePercent + 1;

        Exception exception = assertThrows(InvalidMarketPriceException.class, () -> generateMarketOrderWithPrices(targetPrice, marketPrice));

        assertEquals("Target price exceeds allowable variation", exception.getMessage());
    }

    @Test
    @DisplayName("A target price cannot be lesser than five percent the market price")
    void aMarketOrderWithATargetPriceLesserThanFivePercentLimitTest() {
        Double marketPrice = 100.0;
        Double marketPriceLessFivePercent = marketPrice - marketPrice * 0.5;
        Double targetPrice = marketPriceLessFivePercent - 1;

        Exception exception = assertThrows(InvalidMarketPriceException.class, () -> generateMarketOrderWithPrices(targetPrice, marketPrice));

        assertEquals("Target price exceeds allowable variation", exception.getMessage());

    }

    @Test
    @DisplayName("A market order is initially available")
    void aMarketOrderAnAvailableStatusTest() {
        MarketOrder marketOrder = new MarketOrder();
        assertTrue(marketOrder.getAvailable());
    }

    @Test
    @DisplayName("A sell market order generates a transaction order when the assert quotation is lower or equal than the target price")
    void aSellMarketOrderWithACorrectTargetPriceTest() throws TransactionOrderException {
        MarketOrder sellingMarketOrder = getSellingMarketOrder1();
        Double correctAssetQuote = sellingMarketOrder.getTargetPrice();

        User user = mock(User.class);
        when(user.getEmail()).thenReturn("interested@test.com");

        TransactionOrder transactionOrder = sellingMarketOrder.generateTransactionFor(user, correctAssetQuote);
        assertFalse(sellingMarketOrder.getAvailable());
        assertEquals(TransactionStatus.AWAITING_TRANSFERENCE, transactionOrder.getTransactionStatus());
    }

    @Test
    @DisplayName("A sell market order cannot generate a transaction order when the asset quotation is higher than the target price")
    void aSellMarketOrderWithATargetPriceHigherThanAssertQuotationExceptionTest() throws TransactionOrderException {
        MarketOrder sellingMarketOrder = getSellingMarketOrder1();
        Double higherQuotation = sellingMarketOrder.getMarketPrice() + 1;

        User user = mock(User.class);
        when(user.getEmail()).thenReturn("interested@test.com");

        TransactionOrder transactionOrder = sellingMarketOrder.generateTransactionFor(user, higherQuotation);

        assertTrue(sellingMarketOrder.getAvailable());
        assertEquals(TransactionStatus.CANCELLED_BY_SYSTEM, transactionOrder.getTransactionStatus());

    }

    @Test
    @DisplayName("A purchase market order generates a transaction order when the assert quotation is higher or equal than the target price")
    void aPurchaseMarketOrderWithACorrectTargetPriceTest() throws TransactionOrderException {
        MarketOrder purchaseMarketOrder = getPurchaseMarketOrder1();
        Double correctAssetQuote = purchaseMarketOrder.getTargetPrice();

        User user = mock(User.class);
        when(user.getEmail()).thenReturn("interested@test.com");

        TransactionOrder transactionOrder = purchaseMarketOrder.generateTransactionFor(user, correctAssetQuote);
        assertFalse(purchaseMarketOrder.getAvailable());
        assertEquals(TransactionStatus.AWAITING_TRANSFERENCE, transactionOrder.getTransactionStatus());
    }

    @Test
    @DisplayName("A purchase market order cannot generate a transaction order when the asset quotation is lower than the target price")
    void aPurchaseMarketOrderWithATargetPriceLowerThanAssertQuotationExceptionTest() throws TransactionOrderException {
        MarketOrder purchaseMarketOrder = getPurchaseMarketOrder1();
        Double lowerQuotation = purchaseMarketOrder.getTargetPrice() - 1;

        User user = mock(User.class);
        when(user.getEmail()).thenReturn("interested@test.com");
        TransactionOrder transactionOrder = purchaseMarketOrder.generateTransactionFor(user, lowerQuotation);

        assertTrue(purchaseMarketOrder.getAvailable());
        assertEquals(TransactionStatus.CANCELLED_BY_SYSTEM, transactionOrder.getTransactionStatus());
    }

    @Test
    @DisplayName("A market order is unavailable when a user applies to it")
    void aMarketOrderGetsUnavailableTest() throws TransactionOrderException, PriceExceedsOperationLimitException {
        MarketOrder sellingMarketOrder = getSellingMarketOrder1();
        User interestedUser = mock(User.class);
        when(interestedUser.getEmail()).thenReturn("interested@email.com");

        sellingMarketOrder.generateTransactionFor(interestedUser, sellingMarketOrder.getMarketPrice());

        assertFalse(sellingMarketOrder.getAvailable());
    }

    @Test
    @DisplayName("A market order cannot generate a transaction for the user who created it")
    void aMarketOrderCannotGenerateATransactionForTheSameUserTest() {
        MarketOrder sellingMarketOrder = getSellingMarketOrder1();
        User dealerUser = sellingMarketOrder.getCreator();
        Double validQuotation = sellingMarketOrder.getTargetPrice();

        assertThrows(TransactionOrderException.class, () -> sellingMarketOrder.generateTransactionFor(dealerUser, validQuotation));

    }

    @Test
    @DisplayName("A market order cannot generate a transaction when it is not available ")
    void anUnavailableMarketOrderGeneratesATransactionExceptionTest() throws TransactionOrderException, PriceExceedsOperationLimitException {
        MarketOrder sellingMarketOrder = getSellingMarketOrder1();
        User interestedUser = mock(User.class);
        when(interestedUser.getEmail()).thenReturn("first@test.com");
        User anotherInterestedUser = mock(User.class);
        when(anotherInterestedUser.getEmail()).thenReturn("second@test.com");

        Double validQuotation = sellingMarketOrder.getMarketPrice();

        sellingMarketOrder.generateTransactionFor(interestedUser, validQuotation);

        assertThrows(MarketOrderAlreadyTakenException.class, () -> sellingMarketOrder.generateTransactionFor(anotherInterestedUser, validQuotation));
    }

    private void generateMarketOrderWithPrices(Double targetPrice, Double marketPrice) throws MarketOrderException {
        new MarketOrder(null, null, null, null, marketPrice, targetPrice, null, null);
    }

}
