package ar.edu.unq.desapp.grupog.criptop2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class USDQuotationResponseBody {
    @JsonProperty("d")
    private String date;
    @JsonProperty("v")
    private Double value;
}
