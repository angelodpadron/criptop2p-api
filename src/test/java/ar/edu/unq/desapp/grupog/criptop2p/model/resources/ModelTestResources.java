package ar.edu.unq.desapp.grupog.criptop2p.model.resources;

import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.OperationType;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;

import java.time.LocalDateTime;

public class ModelTestResources {

    private static final User user1 = new User(
            "Juan",
            "Nieve",
            "jnieve@norte.com",
            "jnieve",
            "Norte 1",
            "4608738591410700547451",
            "45821674");

    private static final User user2 = new User(
            "Rhaenyra",
            "Targaryen",
            "rtargaryen@gmail.com",
            "rtargaryen",
            "Oeste 1",
            "4608738591410700547451",
            "45821674");

    private static final MarketOrder marketOrder1;

    static {
        try {
            marketOrder1 = new MarketOrder(
                    null,
                    LocalDateTime.now(),
                    "DOTUSDT",
                    100.0,
                    100.0,
                    100.0,
                    user1,
                    OperationType.SELL);
        } catch (MarketOrderException e) {
            throw new RuntimeException(e);
        }
    }

    public static User getBasicUser1() {
        return user1;
    }

    public static User getBasicUser2() {
        return user2;
    }

    public static MarketOrder getMarketOrder1() {
        return marketOrder1;
    }

}
