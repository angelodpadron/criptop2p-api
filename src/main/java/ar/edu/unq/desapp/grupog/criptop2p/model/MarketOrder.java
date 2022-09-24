package ar.edu.unq.desapp.grupog.criptop2p.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
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
    private Double currentPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;
    @Enumerated(EnumType.STRING)
    private OperationType operation;


}
