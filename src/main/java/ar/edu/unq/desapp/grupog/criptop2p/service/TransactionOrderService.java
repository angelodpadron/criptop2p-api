package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.OperationAmountResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.cryptoquotation.SymbolNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.PriceExceedsOperationLimitException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionStatusException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.user.InvalidConsultationDatesException;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.TransactionOrderRepository;
import ar.edu.unq.desapp.grupog.criptop2p.service.resources.BCRAClient;
import ar.edu.unq.desapp.grupog.criptop2p.service.resources.Mappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static ar.edu.unq.desapp.grupog.criptop2p.service.resources.Mappers.transactionOrderEntityToResponseBody;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionOrderService {
    private final TransactionOrderRepository transactionOrderRepository;
    private final UserService userService;
    private final MarketOrderService marketOrderService;
    private final CryptoQuotationService cryptoQuotationService;
    private final BCRAClient bcraClient;

    public TransactionOrderResponseBody addTransactionOrderToUser(Long marketOrderId) throws TransactionOrderException, PriceExceedsOperationLimitException, MarketOrderNotFoundException, SymbolNotFoundException {
        User interestedUser = userService.getUserLoggedIn();
        MarketOrder marketOrder = marketOrderService.getMarketOrder(marketOrderId);
        Double marketPrice = cryptoQuotationService.getCurrentUsdPriceFor(marketOrder.getCryptocurrency());

        TransactionOrder transactionOrder =
                transactionOrderRepository.save(
                        marketOrder
                                .generateTransactionFor(interestedUser, marketPrice)
                );

        return transactionOrderEntityToResponseBody(transactionOrder);

    }

    public TransactionOrderResponseBody cancelTransactionOrder(Long transactionOrderId) throws TransactionOrderException, TransactionStatusException {
        User cancelingUser = userService.getUserLoggedIn();
        TransactionOrder transactionOrder = getTransactionOrder(transactionOrderId);

        transactionOrder.cancelTransactionFor(cancelingUser);

        return transactionOrderEntityToResponseBody(transactionOrder);

    }

    public TransactionOrderResponseBody performTransferenceFor(Long transactionOrderId) throws TransactionOrderException, TransactionStatusException {
        User payingUser = userService.getUserLoggedIn();
        TransactionOrder transactionOrder = getTransactionOrder(transactionOrderId);

        transactionOrder.notifyTransferenceAs(payingUser);

        return transactionOrderEntityToResponseBody(transactionOrder);

    }

    public TransactionOrderResponseBody confirmReceptionFor(Long transactionOrderId) throws TransactionOrderException, TransactionStatusException {
        User confirmingUser = userService.getUserLoggedIn();
        TransactionOrder transactionOrder = getTransactionOrder(transactionOrderId);

        transactionOrder.notifyReceptionAs(confirmingUser);

        return transactionOrderEntityToResponseBody(transactionOrder);

    }

    private TransactionOrder getTransactionOrder(Long transactionOrderId) throws TransactionOrderException {
        return transactionOrderRepository
                .findById(transactionOrderId)
                .orElseThrow(() -> new TransactionOrderException("The transaction could not be found"));
    }

    public List<TransactionOrderResponseBody> getAllTransactionOrders() {
        User user = userService.getUserLoggedIn();
        return user.getTransactionOrders().stream().map(Mappers::transactionOrderEntityToResponseBody).toList();
    }

    public List<TransactionOrder> getClosedTransactionsBetweenDates(LocalDateTime fromDate, LocalDateTime toDate) throws InvalidConsultationDatesException {
        User user = userService.getUserLoggedIn();
        if (fromDate.isAfter(toDate)) {
            throw new InvalidConsultationDatesException("The start date must be before the end date");
        }

        return transactionOrderRepository.getUserClosedTransactionsBetweenDates(user.getId(), fromDate, toDate);
    }

    public OperationAmountResponseBody getOperationsAmountBetweenDates(LocalDateTime fromDate, LocalDateTime toDate) throws InvalidConsultationDatesException {
        List<TransactionOrder> closedTransactions = getClosedTransactionsBetweenDates(fromDate, toDate);

        Double totalDollarAmount =
                closedTransactions
                        .stream()
                        .mapToDouble(transactionOrder -> transactionOrder.getMarketOrder().getTargetPrice())
                        .sum();

        Double totalPesosAmount = totalDollarAmount * bcraClient.getLastUSDARSQuotation();

        return Mappers.toOperationAmountResponseBody(
                totalDollarAmount,
                totalPesosAmount,
                closedTransactions.stream().map(Mappers::transactionOrderEntityToResponseBody).toList());

    }
}
