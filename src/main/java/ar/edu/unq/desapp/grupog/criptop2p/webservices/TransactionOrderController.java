package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.exception.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.service.TransactionOrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionOrderController {

    private final TransactionOrderService transactionOrderService;

    @Operation(summary = "Cancel a transaction order with a given id")
    @PostMapping(path = "/cancel/{transaction_id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void cancelTransactionOrder(@PathVariable Long transaction_id) throws TransactionOrderException {
        transactionOrderService.cancelTransactionOrder(transaction_id);
    }

}
