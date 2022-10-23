package ar.edu.unq.desapp.grupog.criptop2p.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionOrderResponseBody {
    @JsonProperty("transaction_id")
    private long transactionOrderId;
    @JsonProperty("market_order")
    private MarketOrderResponseBody marketOrderResponseBody;
    @JsonProperty("creation_date")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;
    @JsonProperty("interested_user")
    private String interestedUser;
    @JsonProperty("dealer_user")
    private String dealerUser;
    @JsonProperty("transaction_status")
    private String transactionStatus;
}
