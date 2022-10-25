package ar.edu.unq.desapp.grupog.criptop2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OperationAmountResponseBody {
    @JsonProperty("amount_traded_in_dollars")
    private Double amountTradedInDollars;
    @JsonProperty("amount_traded_in_pesos")
    private Double amountTradedInPesos;
    @JsonProperty("transactions_details")
    private List<TransactionOrderResponseBody> transactionsDetails;
}
