package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.cryptoquotation.SymbolNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.MarketOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static ar.edu.unq.desapp.grupog.criptop2p.model.resources.ModelTestResources.getBasicUser1;
import static ar.edu.unq.desapp.grupog.criptop2p.model.resources.ModelTestResources.getSellingMarketOrder1;
import static ar.edu.unq.desapp.grupog.criptop2p.model.resources.ServiceTestResources.getMarketOrderRequestBodyFromEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MarketOrderServiceTest {

    @Mock
    private MarketOrderRepository marketOrderRepository;
    @Mock
    private UserService userService;
    @Mock
    private CryptoQuotationService cryptoQuotationService;

    @Autowired
    @InjectMocks
    private MarketOrderService marketOrderService;
    private MarketOrder sellingMarketOrder1;
    private MarketOrderRequestBody sellingMarketOrderRequestBody1;
    private User user1;

    @BeforeEach
    void setUp() {
        sellingMarketOrder1 = getSellingMarketOrder1();
        sellingMarketOrderRequestBody1 = getMarketOrderRequestBodyFromEntity(sellingMarketOrder1);
        user1 = getBasicUser1();
    }

    @DisplayName("A market order can be assigned to a user")
    @Test
    void assigningMarketOrderToAUserTest() throws MarketOrderException, SymbolNotFoundException {
        when(userService.getUserLoggedIn()).thenReturn(user1);
        when(cryptoQuotationService.getCurrentUsdPriceFor(any())).thenReturn(sellingMarketOrder1.getTargetPrice());

        marketOrderService.addMarketOrderToUser(sellingMarketOrderRequestBody1);

        verify(marketOrderRepository).save(any());
        assertTrue(user1.getMarketOrders().stream().map(MarketOrder::getCryptocurrency).toList().contains(sellingMarketOrder1.getCryptocurrency()));

    }

    @DisplayName("A market order can be retrieved by id")
    @Test
    void retrievingMarketOrderByIdTest() throws MarketOrderNotFoundException {
        when(marketOrderRepository.findById(sellingMarketOrder1.getId())).thenReturn(Optional.ofNullable(sellingMarketOrder1));
        MarketOrder retrievedMarketOrder = marketOrderService.getMarketOrder(sellingMarketOrder1.getId());
        assertEquals(sellingMarketOrder1, retrievedMarketOrder);
    }

    @DisplayName("When a market order cannot be retrieved by id, an exception is thrown")
    @Test
    void retrievingMarketOrderByIdExceptionTest() {
        assertThrows(MarketOrderNotFoundException.class, () -> marketOrderService.getMarketOrder(sellingMarketOrder1.getId()));
    }

    @DisplayName("All market orders created can be retrieved")
    @Test
    void retrievingAllMarketOrdersCreatedTest() {
        when(marketOrderRepository.findAll()).thenReturn(List.of(sellingMarketOrder1));
        List<MarketOrderResponseBody> retrievedMarketOrders = marketOrderService.getMarketOrders();

        assertFalse(retrievedMarketOrders.isEmpty());
    }

    @DisplayName("All market orders created by the consulting user can be retrieved")
    @Test
    void retrievingAllMarketOrdersCreatedByUserTest() {
        when(userService.getUserLoggedIn()).thenReturn(user1);
        user1.addMarketOrder(sellingMarketOrder1);

        List<MarketOrderResponseBody> retrievedMarketOrders = marketOrderService.getUserMarketOrders();

        assertFalse(retrievedMarketOrders.isEmpty());
    }


}
