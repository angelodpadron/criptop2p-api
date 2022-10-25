package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionStatusException;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.MarketOrderRepository;
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
    private final MarketOrderRepository marketOrderRepository;
    private final UserService userService;

    public TransactionOrderResponseBody addTransactionOrderToUser(Long marketOrderId) throws TransactionOrderException, MarketOrderException {
        User interestedUser = userService.getUserLoggedIn();
        MarketOrder marketOrder = marketOrderRepository
                .findById(marketOrderId)
                .orElseThrow(() -> new TransactionOrderException("Market order not found"));

        TransactionOrder transactionOrder = marketOrder.generateTransaction(interestedUser);
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

        transactionOrder.performTransferenceAs(payingUser);

        return transactionOrderEntityToResponseBody(transactionOrder);

    }

    public TransactionOrderResponseBody confirmReceptionFor(Long transactionOrderId) throws TransactionOrderException, TransactionStatusException {
        User confirmingUser = userService.getUserLoggedIn();
        TransactionOrder transactionOrder = getTransactionOrder(transactionOrderId);

        transactionOrder.confirmReceptionAs(confirmingUser);

        return transactionOrderEntityToResponseBody(transactionOrder);

    }

    private TransactionOrder getTransactionOrder(Long transactionOrderId) throws TransactionOrderException {
        return transactionOrderRepository
                .findById(transactionOrderId)
                .orElseThrow(() -> new TransactionOrderException("The transaction could not be found"));
    }
}
