package ar.edu.unq.desapp.grupog.criptop2p.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationData {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cryptoquotation_id")
    CryptoQuotation cryptoQuotation;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @JsonProperty("price_in_usd")
    @Column(name = "PRICE_IN_USD")
    private Double priceInUSD;
    @JsonProperty("price_in_ars")
    @Column(name = "PRICE_IN_ARS")
    private Double priceInARS;
    @JsonProperty("last_update")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastUpdate;

    public QuotationData(CryptoQuotation cryptoQuotation, Double priceInUSD, Double priceInARS) {
        this.cryptoQuotation = cryptoQuotation;
        this.priceInUSD = priceInUSD;
        this.priceInARS = priceInARS;
        lastUpdate = LocalDateTime.now();
    }
}
