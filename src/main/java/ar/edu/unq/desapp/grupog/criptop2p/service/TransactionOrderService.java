package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.MarketOrderRepository;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.TransactionOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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
        Optional<MarketOrder> optionalMarketOrder = marketOrderRepository.findById(marketOrderId);

        if (optionalMarketOrder.isPresent()) {
            MarketOrder marketOrder = optionalMarketOrder.get();
            TransactionOrder transactionOrder = interestedUser.applyTo(marketOrder);
            transactionOrderRepository.save(transactionOrder);

            return transactionOrderEntityToResponseBody(transactionOrder);
        }

        throw new TransactionOrderException("Market order not found");

    }

    public void cancelTransactionOrder(Long transactionOrderId) throws TransactionOrderException {
        User cancelingUser = userService.getUserLoggedIn();
        Optional<TransactionOrder> optionalTransactionOrder = transactionOrderRepository.findById(transactionOrderId);

        if (optionalTransactionOrder.isPresent()) {
            TransactionOrder transactionOrder = optionalTransactionOrder.get();
            transactionOrder.cancelTransactionFor(cancelingUser);
        } else {
            throw new TransactionOrderException("Transaction order not found");
        }

    }


}
