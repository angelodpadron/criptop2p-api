package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.PriceExceedsOperationLimitException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionStatusException;
import ar.edu.unq.desapp.grupog.criptop2p.model.CryptoQuotation;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.TransactionOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static ar.edu.unq.desapp.grupog.criptop2p.service.resources.Mappers.transactionOrderEntityToResponseBody;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionOrderService {
    private final TransactionOrderRepository transactionOrderRepository;
    private final MarketOrderService marketOrderService;
    private final CryptoQuotationService cryptoQuotationService;
    private final UserService userService;

    public TransactionOrderResponseBody addTransactionOrderToUser(Long marketOrderId) throws TransactionOrderException, PriceExceedsOperationLimitException {
        User interestedUser = userService.getUserLoggedIn();
        MarketOrder marketOrder = marketOrderService.getMarketOrder(marketOrderId);
        CryptoQuotation cryptoQuotation = cryptoQuotationService.getQuotation(marketOrder.getCryptocurrency());

        TransactionOrder transactionOrder = marketOrder.generateTransactionFor(interestedUser, cryptoQuotation.getPriceInUSD());
        transactionOrderRepository.save(transactionOrder);

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
}
