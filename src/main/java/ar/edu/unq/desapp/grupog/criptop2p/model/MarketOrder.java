package ar.edu.unq.desapp.grupog.criptop2p.model;

import ar.edu.unq.desapp.grupog.criptop2p.exception.InvalidOperationPriceException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.MarketOrderException;
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
            throw new InvalidOperationPriceException("Target price exceeds allowable variation");
        }
    }

    private boolean targetPriceInRange(Double marketPrice, Double targetPrice) {
        Double maxAllowedPrice = marketPrice + marketPrice * priceMarginAllowed;
        Double minAllowedPrice = marketPrice - marketPrice * priceMarginAllowed;
        return targetPrice >= minAllowedPrice && targetPrice <= maxAllowedPrice;
    }

    public TransactionOrder generateTransaction(User interestedUser) throws MarketOrderException {
        if (interestedUser.getEmail().equals(creator.getEmail())) {
            throw new MarketOrderException("User cannot apply to their own market order");
        }
        this.available = false;
        TransactionOrder transactionOrder = TransactionOrder.generateFor(this, interestedUser);
        return transactionOrder;
    }
}
