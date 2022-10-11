package ar.edu.unq.desapp.grupog.criptop2p.dto;

import ar.edu.unq.desapp.grupog.criptop2p.model.OperationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MarketOrderRequestBody {
    private String cryptocurrency;
    @JsonProperty("nominal_amount")
    private Double nominalAmount;
    @JsonProperty("target_price")
    private Double targetPrice;
    private OperationType operation;
}
