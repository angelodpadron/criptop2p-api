package ar.edu.unq.desapp.grupog.criptop2p.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoQuotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    @JsonProperty("price_in_usd")
    private Double priceInUSD;
    @JsonProperty("price_in_ars")
    private Double priceInARS;
    @JsonProperty("last_update")
    private LocalDateTime lastUpdate;

    public CryptoQuotation(String symbol, Double priceInUSD, Double priceInARS) {
        this.symbol = symbol;
        this.priceInUSD = priceInUSD;
        this.priceInARS = priceInARS;
        this.lastUpdate = LocalDateTime.now();
    }

}
