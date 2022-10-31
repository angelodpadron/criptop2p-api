package ar.edu.unq.desapp.grupog.criptop2p.model;

import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.InvalidMarketPriceException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.PriceExceedsOperationLimitException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.MarketOrderAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@Data
@Entity
@Table(name = "MARKET_ORDERS")
public class MarketOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime creationDate;
    private String cryptocurrency;
    private Double nominalAmount;
    private Double marketPrice;
    private Double targetPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;
    @Enumerated(EnumType.STRING)
    private OperationType operation;
    private Boolean available = true;
    @Transient
    private Double priceMarginAllowed = 0.5;

    public MarketOrder(Long id,
                       LocalDateTime creationDate,
                       String cryptocurrency,
                       Double nominalAmount,
                       Double marketPrice,
                       Double targetPrice,
                       User creator,
                       OperationType operation) throws MarketOrderException {
        this.id = id;
        this.creationDate = creationDate;
        this.cryptocurrency = cryptocurrency;
        this.nominalAmount = nominalAmount;
        this.marketPrice = marketPrice;
        this.creator = creator;
        this.operation = operation;

        if (targetPriceInRange(marketPrice, targetPrice)) {
            this.targetPrice = targetPrice;
        } else {
            throw new InvalidMarketPriceException("Target price exceeds allowable variation");
        }
    }

    public TransactionOrder generateTransactionFor(User interestedUser, Double currentQuotation) throws TransactionOrderException, PriceExceedsOperationLimitException {
        checkIfUserCanApply(interestedUser);

        TransactionOrder transactionOrder = new TransactionOrder(this, interestedUser);
        creator.addTransactionOrder(transactionOrder);
        interestedUser.addTransactionOrder(transactionOrder);

        checkIfTargetPriceIsValidForOperation(currentQuotation, transactionOrder);

        available = false;

        return transactionOrder;
    }

    private void checkIfUserCanApply(User interestedUser) throws TransactionOrderException {
        if (!available) {
            throw new MarketOrderAlreadyTakenException("The market order was already taken");
        }
        if (interestedUser.hasSameEmail(creator)) {
            throw new TransactionOrderException("A user cannot apply for is own market order");
        }
    }

    private void checkIfTargetPriceIsValidForOperation(Double currentQuotation, TransactionOrder transactionOrder) throws PriceExceedsOperationLimitException {
        if (!operation.priceIsValid(currentQuotation, targetPrice)) {
            transactionOrder.cancelTransactionAsSystem();
            throw new PriceExceedsOperationLimitException("The market order price exceeds the limit for the type of operation");
        }
    }

    private boolean targetPriceInRange(Double marketPrice, Double targetPrice) {
        Double maxAllowedPrice = marketPrice + marketPrice * priceMarginAllowed;
        Double minAllowedPrice = marketPrice - marketPrice * priceMarginAllowed;
        return targetPrice >= minAllowedPrice && targetPrice <= maxAllowedPrice;
    }

}
