package ar.edu.unq.desapp.grupog.criptop2p.dto;

import ar.edu.unq.desapp.grupog.criptop2p.model.OperationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MarketOrderResponseBody {
    @JsonProperty("market_order_id")
    private Long id;
    @JsonProperty("user")
    private String userEmail;
    @JsonProperty("user_reputation")
    private int userReputation;
    @JsonProperty("user_operation_amount")
    private int userOperationAmount;
    @JsonProperty("creation_date")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;
    private String cryptocurrency;
    @JsonProperty("nominal_amount")
    private Double nominalAmount;
    @JsonProperty("market_price")
    private Double marketPrice;
    @JsonProperty("target_price")
    private Double targetPrice;
    private OperationType operation;
    private Boolean available;


}
