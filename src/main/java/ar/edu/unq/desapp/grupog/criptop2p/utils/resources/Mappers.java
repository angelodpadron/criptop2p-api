package ar.edu.unq.desapp.grupog.criptop2p.utils.resources;

import ar.edu.unq.desapp.grupog.criptop2p.dto.*;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.*;

import java.time.LocalDateTime;
import java.util.List;

public final class Mappers {

    private Mappers() {
    }

    // Crypto quotation mappings

    public static CryptoQuotationResponseBody cryptoQuotationEntityTo24HoursResponseBody(CryptoQuotation cryptoQuotation) {
        List<QuotationDataResponseBody> quotationDataResponseBodies =
                cryptoQuotation
                        .getQuotationData()
                        .stream()
                        .map(quotationData ->
                                new QuotationDataResponseBody(quotationData.getPriceInUSD(), quotationData.getPriceInARS(), quotationData.getLastUpdate()))
                        .toList();

        return new CryptoQuotationResponseBody(cryptoQuotation.getSymbol(), quotationDataResponseBodies);
    }

    public static CurrentCryptoQuotationResponseBody cryptoQuotationEntityToResponseBody(CryptoQuotation cryptoQuotation) {
        QuotationData quotationData = cryptoQuotation.getLastQuotation();
        return new CurrentCryptoQuotationResponseBody(cryptoQuotation.getSymbol(), quotationData.getPriceInUSD(), quotationData.getPriceInARS(), quotationData.getLastUpdate());
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


    public static UserSummaryResponseBody userEntityToSummaryResponseBody(User user) {
        return new UserSummaryResponseBody(user.getFirstname(), user.getLastname(), user.getOperations(), user.getReputation());
    }
}
