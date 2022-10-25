package ar.edu.unq.desapp.grupog.criptop2p.service.resources;

import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.OperationAmountResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;

import java.time.LocalDateTime;
import java.util.List;

public final class Mappers {

    private Mappers() {
    }

    // Transaction order mappings

    public static TransactionOrderResponseBody transactionOrderEntityToResponseBody(TransactionOrder transactionOrder) {
        return new TransactionOrderResponseBody(
                transactionOrder.getId(),
                marketOrderEntityToResponseBody(transactionOrder.getMarketOrder()),
                transactionOrder.getCreationDate(),
                transactionOrder.getInterestedUser().getEmail(),
                transactionOrder.getDealerUser().getEmail(),
                transactionOrder.getTransactionStatus().toString()
        );
    }

    // Market order mappings

    public static MarketOrderResponseBody marketOrderEntityToResponseBody(MarketOrder marketOrder) {
        return new MarketOrderResponseBody(
                marketOrder.getId(),
                marketOrder.getCreator().getEmail(),
                marketOrder.getCreator().getReputation(),
                marketOrder.getCreator().getOperations(),
                marketOrder.getCreationDate(),
                marketOrder.getCryptocurrency(),
                marketOrder.getNominalAmount(),
                marketOrder.getMarketPrice(),
                marketOrder.getTargetPrice(),
                marketOrder.getOperation(),
                marketOrder.getAvailable());
    }

    public static MarketOrder marketOrderRequestBodyToEntity(MarketOrderRequestBody marketOrderRequestBody,
                                                             User user,
                                                             Double marketPrice) throws MarketOrderException {
        return new MarketOrder(
                null,
                LocalDateTime.now(),
                marketOrderRequestBody.getCryptocurrency(),
                marketOrderRequestBody.getNominalAmount(),
                marketPrice,
                marketOrderRequestBody.getTargetPrice(),
                user,
                marketOrderRequestBody.getOperation());
    }

    public static OperationAmountResponseBody toOperationAmountResponseBody(Double usdAmount, Double pesosAmount, List<TransactionOrderResponseBody> transactionsDetails) {
        return new OperationAmountResponseBody(usdAmount, pesosAmount, transactionsDetails);
    }


}
