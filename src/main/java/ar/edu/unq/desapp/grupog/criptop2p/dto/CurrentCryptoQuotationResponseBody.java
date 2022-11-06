package ar.edu.unq.desapp.grupog.criptop2p.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentCryptoQuotationResponseBody {
    private String symbol;
    @JsonProperty("price_in_usd")
    private Double priceInUsd;
    @JsonProperty("price_in_ars")
    private Double priceInArs;
    @JsonProperty("last_update")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastUpdate;
}
