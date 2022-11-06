package ar.edu.unq.desapp.grupog.criptop2p.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawCryptoQuotationResponseBody {
    private String symbol;
    private Double price;
}
