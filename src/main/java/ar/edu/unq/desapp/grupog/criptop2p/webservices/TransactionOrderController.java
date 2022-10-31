package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionStatusException;
import ar.edu.unq.desapp.grupog.criptop2p.service.TransactionOrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionOrderController {

    private final TransactionOrderService transactionOrderService;

    @Operation(summary = "Cancel a transaction order with a given id")
    @PostMapping(path = "/cancel/{transaction_id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<TransactionOrderResponseBody> cancelTransactionOrder(@PathVariable("transaction_id") Long transactionId) throws TransactionOrderException, TransactionStatusException {
        return ResponseEntity.ok().body(transactionOrderService.cancelTransactionOrder(transactionId));
    }

    @Operation(summary = "Perform the transference of a transaction order")
    @PostMapping(path = "/transfer/{transaction_id}")
    @ResponseBody
    public ResponseEntity<TransactionOrderResponseBody> performTransferenceFor(@PathVariable("transaction_id") Long transactionId) throws TransactionOrderException, TransactionStatusException {
        return ResponseEntity.ok().body(transactionOrderService.performTransferenceFor(transactionId));
    }

    @Operation(summary = "Confirm the reception of a transaction order")
    @PostMapping(path = "/confirm/{transaction_id}")
    @ResponseBody
    public ResponseEntity<TransactionOrderResponseBody> confirmReceptionFor(@PathVariable("transaction_id") Long transactionId) throws TransactionOrderException, TransactionStatusException {
        return ResponseEntity.ok().body(transactionOrderService.confirmReceptionFor(transactionId));
    }

}
