package ar.edu.unq.desapp.grupog.criptop2p;

import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;

public class ServiceTestResources {

    public static UserRequestBody getUserRequestBodyFromEntity(User user) {
        return new UserRequestBody(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.getAddress(),
                user.getCvuMercadoPago(),
                user.getWalletAddress()
        );
    }

    public static MarketOrderRequestBody getMarketOrderRequestBodyFromEntity(MarketOrder marketOrder) {
        return new MarketOrderRequestBody(
                marketOrder.getCryptocurrency(),
                marketOrder.getNominalAmount(),
                marketOrder.getTargetPrice(),
                marketOrder.getOperation()
        );
    }

}
