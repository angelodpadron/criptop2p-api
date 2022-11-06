package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.cryptoquotation.SymbolNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.MarketOrderRepository;
import ar.edu.unq.desapp.grupog.criptop2p.service.resources.Mappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static ar.edu.unq.desapp.grupog.criptop2p.service.resources.Mappers.marketOrderRequestBodyToEntity;

@Service
@RequiredArgsConstructor
public class MarketOrderService {

    private final MarketOrderRepository marketOrderRepository;
    private final UserService userService;
    private final CryptoQuotationService cryptoQuotationService;

    public void addMarketOrderToUser(MarketOrderRequestBody marketOrderRequestBody) throws MarketOrderException, SymbolNotFoundException {
        User user = userService.getUserLoggedIn();
        Double marketPrice = cryptoQuotationService.getCurrentUsdPriceFor(marketOrderRequestBody.getCryptocurrency());
        MarketOrder marketOrder = marketOrderRequestBodyToEntity(marketOrderRequestBody, user, marketPrice);

        marketOrderRepository.save(marketOrder);
        user.addMarketOrder(marketOrder);

    }

    public MarketOrder getMarketOrder(Long marketOrderId) throws MarketOrderNotFoundException {
        return marketOrderRepository.findById(marketOrderId).orElseThrow(() -> new MarketOrderNotFoundException("Market order not found"));
    }

    public List<MarketOrderResponseBody> getMarketOrders() {
        List<MarketOrder> marketOrders = marketOrderRepository.findAll();
        return marketOrders.stream().map(Mappers::marketOrderEntityToResponseBody).toList();

    }

    public List<MarketOrderResponseBody> getUserMarketOrders() {
        User user = userService.getUserLoggedIn();
        List<MarketOrder> marketOrders = user.getMarketOrders();

        return marketOrders.stream().map(Mappers::marketOrderEntityToResponseBody).toList();
    }

}
