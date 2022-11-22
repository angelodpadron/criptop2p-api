package ar.edu.unq.desapp.grupog.criptop2p;

import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.OperationType;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;

import java.time.LocalDateTime;

public class ModelTestResources {

    public static User getBasicUser1() {
        return new User(
                "Juan",
                "Nieve",
                "jnieve@norte.com",
                "Password@1",
                "Norte 1",
                "4608738591410700547451",
                "45821674");
    }

    public static User getBasicUser2() {
        return new User(
                "Rhaenyra",
                "Targaryen",
                "rtargaryen@gmail.com",
                "Password@1",
                "Oeste 1",
                "4608738591410700547451",
                "45821674");
    }

    public static User getBasicUser3() {
        return new User(
                "Aemma",
                "Arryn",
                "arryn@gmail.com",
                "Password@1",
                "Oeste 1",
                "4608738590014107545174",
                "82744516");
    }

    public static MarketOrder getSellingMarketOrder1() {
        try {
            return new MarketOrder(
                    null,
                    LocalDateTime.now(),
                    "DOTUSDT",
                    100.0,
                    100.0,
                    100.0,
                    getBasicUser1(),
                    OperationType.SELL);
        } catch (MarketOrderException e) {
            throw new RuntimeException(e);
        }
    }

    public static MarketOrder getPurchaseMarketOrder1() {
        try {
            return new MarketOrder(
                    null,
                    LocalDateTime.now(),
                    "DOTUSDT",
                    100.0,
                    100.0,
                    100.0,
                    getBasicUser1(),
                    OperationType.PURCHASE);
        } catch (MarketOrderException e) {
            throw new RuntimeException(e);
        }
    }


    public static TransactionOrder getSellingTransactionOrder1() {
        return new TransactionOrder(getSellingMarketOrder1(), getBasicUser2());
    }

    public static TransactionOrder getPurchaseTransactionOrder1() {
        return new TransactionOrder(getPurchaseMarketOrder1(), getBasicUser2());
    }

    public static TransactionOrder getSellingTransactionOrderWithId(Long id) {
        TransactionOrder transactionOrder = new TransactionOrder(getSellingMarketOrder1(), getBasicUser2());
        transactionOrder.setId(id);
        return transactionOrder;
    }
}
