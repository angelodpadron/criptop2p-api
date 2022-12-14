package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.cryptoquotation.SymbolNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.marketorder.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.transactionorder.TransactionOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.service.MarketOrderService;
import ar.edu.unq.desapp.grupog.criptop2p.service.TransactionOrderService;
import ar.edu.unq.desapp.grupog.criptop2p.utils.aspects.LogExecutionTime;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/marketorders")
@RequiredArgsConstructor
@Slf4j
@LogExecutionTime
public class MarketOrderController {

    private final MarketOrderService marketOrderService;
    private final TransactionOrderService transactionOrderService;

    @Operation(summary = "Get all market orders")
    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<MarketOrderResponseBody>> getMarketOrders() {
        return ResponseEntity.ok().body(marketOrderService.getMarketOrders());
    }

    @Operation(summary = "Get all market orders created by the user")
    @GetMapping("/created")
    @ResponseBody
    public ResponseEntity<List<MarketOrderResponseBody>> getAllUserMarketOrders() {
        return ResponseEntity.ok().body(marketOrderService.getUserMarketOrders());
    }

    @Operation(summary = "Create a market order")
    @PostMapping(path = "/create")
    @ResponseBody
    public ResponseEntity<MarketOrderResponseBody> createMarketOrder(@RequestBody MarketOrderRequestBody marketOrderRequestBody) throws MarketOrderException, SymbolNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(marketOrderService.addMarketOrderToUser(marketOrderRequestBody));
    }

    @Operation(summary = "Apply for a market order")
    @PostMapping(path = "/apply/{id}")
    @ResponseBody
    public ResponseEntity<TransactionOrderResponseBody> applyToMarketOrder(@PathVariable Long id) throws TransactionOrderException, MarketOrderException, SymbolNotFoundException {
        return ResponseEntity.ok().body(transactionOrderService.addTransactionOrderToUser(id));
    }

}
