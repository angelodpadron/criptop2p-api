package ar.edu.unq.desapp.grupog.criptop2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CryptoQuotationResponseBody {
    private String symbol;
    @JsonProperty("quotations")
    List<QuotationDataResponseBody> quotations;
}
