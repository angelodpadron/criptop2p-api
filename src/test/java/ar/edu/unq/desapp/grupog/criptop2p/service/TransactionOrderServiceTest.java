package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.OperationAmountResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.cryptoquotation.SymbolNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.InvalidMarketPriceException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionStatusException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.user.InvalidConsultationDatesException;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.TransactionOrderRepository;
import ar.edu.unq.desapp.grupog.criptop2p.utils.resources.BCRAClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ar.edu.unq.desapp.grupog.criptop2p.model.TransactionStatus.*;
import static ar.edu.unq.desapp.grupog.criptop2p.model.resources.ModelTestResources.getSellingTransactionOrderWithId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TransactionOrderServiceTest {

    @Mock
    private TransactionOrderRepository transactionOrderRepository;

    @Mock
    private UserService userService;
    @Mock
    private MarketOrderService marketOrderService;
    @Mock
    private CryptoQuotationService cryptoQuotationService;
    @Mock
    private BCRAClient bcraClient;


    @Autowired
    @InjectMocks
    private TransactionOrderService transactionOrderService;

    private User userMock;
    private TransactionOrder sellingTransactionOrder1;
    private TransactionOrder sellingTransactionOrder2;


    @BeforeEach
    void setUp() {
        userMock = mock(User.class);
        sellingTransactionOrder1 = getSellingTransactionOrderWithId(1L);
        sellingTransactionOrder2 = getSellingTransactionOrderWithId(2L);
    }

    @DisplayName("The service generates a transaction order and return a transaction response body")
    @Test
    void generateATransactionOrderAndReturnResponseBodyTest() throws MarketOrderNotFoundException, TransactionOrderException, SymbolNotFoundException, InvalidMarketPriceException {
        TransactionOrder sellingTransactionOrder = getSellingTransactionOrderWithId(1L);

        User dealerUser = sellingTransactionOrder.getDealerUser();
        User interestedUser = sellingTransactionOrder.getInterestedUser();
        MarketOrder marketOrder = sellingTransactionOrder.getMarketOrder();

        when(userService.getUserLoggedIn()).thenReturn(interestedUser);
        when(marketOrderService.getMarketOrder(any())).thenReturn(marketOrder);
        when(cryptoQuotationService.getCurrentUsdPriceFor(any())).thenReturn(marketOrder.getTargetPrice());
        when(transactionOrderRepository.save(any())).thenReturn(sellingTransactionOrder);

        TransactionOrderResponseBody responseBody = transactionOrderService.addTransactionOrderToUser(marketOrder.getId());

        assertEquals(dealerUser.getEmail(), responseBody.getDealerUser());
        assertEquals(interestedUser.getEmail(), responseBody.getInterestedUser());

    }

    @DisplayName("The service can retrieve all transaction orders from the consulting user")
    @Test
    void retrieveAllTransactionOrdersFromAUserTest() {
        when(userMock.getTransactionOrders()).thenReturn(List.of(sellingTransactionOrder1));
        when(userService.getUserLoggedIn()).thenReturn(userMock);

        List<TransactionOrderResponseBody> responseBodies = transactionOrderService.getAllTransactionOrders();

        assertFalse(responseBodies.isEmpty());
        assertEquals(sellingTransactionOrder1.getId(), responseBodies.get(0).getTransactionOrderId());
    }

    @DisplayName("When a transaction is cancelled, the service returns a response body with the details of cancellation")
    @Test
    void transactionCancellationTest() throws TransactionOrderException, TransactionStatusException {
        TransactionOrder sellingTransactionOrder = getSellingTransactionOrderWithId(1L);
        User dealerUser = sellingTransactionOrder.getDealerUser();

        when(userService.getUserLoggedIn()).thenReturn(dealerUser);
        when(transactionOrderRepository.findById(any())).thenReturn(Optional.of(sellingTransactionOrder));

        TransactionOrderResponseBody responseBody = transactionOrderService.cancelTransactionOrder(sellingTransactionOrder.getId());

        assertEquals(sellingTransactionOrder.getId(), responseBody.getTransactionOrderId());
        assertEquals(CANCELLED.toString(), responseBody.getTransactionStatus());

    }

    @DisplayName("When the transaction transference is notified, the service returns a response body with the details of the operation")
    @Test
    void transactionTransferenceNotificationTest() throws TransactionOrderException, TransactionStatusException {
        User interestedUser = sellingTransactionOrder1.getInterestedUser();
        when(userService.getUserLoggedIn()).thenReturn(interestedUser);
        when(transactionOrderRepository.findById(any())).thenReturn(Optional.of(sellingTransactionOrder1));

        TransactionOrderResponseBody responseBody = transactionOrderService.performTransferenceFor(sellingTransactionOrder1.getId());

        assertEquals(sellingTransactionOrder1.getId(), responseBody.getTransactionOrderId());
        assertEquals(AWAITING_RECEPTION.toString(), responseBody.getTransactionStatus());
    }

    @DisplayName("When the transference reception is notified, the service returns a response body with the details of the operation")
    @Test
    void transactionReceptionNotificationTest() throws TransactionOrderException, TransactionStatusException {
        TransactionOrder sellingTransactionOrder = getSellingTransactionOrderWithId(1L);
        sellingTransactionOrder.setTransactionStatus(AWAITING_RECEPTION);
        User dealerUser = sellingTransactionOrder.getDealerUser();
        when(userService.getUserLoggedIn()).thenReturn(dealerUser);
        when(transactionOrderRepository.findById(any())).thenReturn(Optional.of(sellingTransactionOrder));

        TransactionOrderResponseBody responseBody = transactionOrderService.confirmReceptionFor(sellingTransactionOrder.getId());

        assertEquals(sellingTransactionOrder.getId(), responseBody.getTransactionOrderId());
        assertEquals(CLOSED.toString(), responseBody.getTransactionStatus());
    }

    @DisplayName("An exception is thrown when trying to operate an unsaved transaction order")
    @Test
    void operatingAnUnsavedTransactionOrderExceptionTest() {
        assertThrows(TransactionOrderException.class, () -> transactionOrderService.cancelTransactionOrder(sellingTransactionOrder1.getId()));
    }

    @DisplayName("The transactions of a user between two dates can be retrieved")
    @Test
    void retrieveTransactionOrdersFromUserBetweenTwoDatesTest() throws InvalidConsultationDatesException {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        when(userMock.getId()).thenReturn(1L);
        when(userService.getUserLoggedIn()).thenReturn(userMock);

        transactionOrderService.getClosedTransactionsBetweenDates(start, end);

        verify(transactionOrderRepository).getUserClosedTransactionsBetweenDates(userMock.getId(), start, end);

    }

    @DisplayName("The transactions of a user between two dates cannot be retrieved if the starting date is after the ending date")
    @Test
    void retrieveTransactionOrdersFromUserBetweenTwoDatesExceptionTest() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now();

        assertThrows(InvalidConsultationDatesException.class, () -> transactionOrderService.getClosedTransactionsBetweenDates(start, end));
    }

    @DisplayName("The service can generate a summary of the closed transactions of a user between two dates")
    @Test
    void generateSummaryOfClosedTransactionTest() throws InvalidConsultationDatesException {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        sellingTransactionOrder1.setTransactionStatus(CLOSED);
        sellingTransactionOrder2.setTransactionStatus(CLOSED);


        when(userMock.getId()).thenReturn(1L);
        when(userService.getUserLoggedIn()).thenReturn(userMock);
        when(transactionOrderRepository.getUserClosedTransactionsBetweenDates(userMock.getId(), start, end))
                .thenReturn(List.of(sellingTransactionOrder1, sellingTransactionOrder2));
        when(bcraClient.getLastUSDARSQuotation()).thenReturn(100.0);

        Double totalInUsd = sellingTransactionOrder1.getMarketOrder().getTargetPrice() + sellingTransactionOrder2.getMarketOrder().getTargetPrice();
        Double totalInArs = totalInUsd * bcraClient.getLastUSDARSQuotation();

        OperationAmountResponseBody responseBody = transactionOrderService.getOperationsAmountBetweenDates(start, end);

        assertFalse(responseBody.getTransactionsDetails().isEmpty());
        assertEquals(totalInUsd, responseBody.getAmountTradedInDollars());
        assertEquals(totalInArs, responseBody.getAmountTradedInPesos());

    }


}
