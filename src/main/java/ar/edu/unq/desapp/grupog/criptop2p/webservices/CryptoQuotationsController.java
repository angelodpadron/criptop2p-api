package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.CryptoQuotationResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.CurrentCryptoQuotationResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.cryptoquotation.SymbolNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.service.CryptoQuotationService;
import ar.edu.unq.desapp.grupog.criptop2p.utils.aspects.LogExecutionTime;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/quotations")
@RequiredArgsConstructor
@Slf4j
@LogExecutionTime
public class CryptoQuotationsController {

    private final CryptoQuotationService cryptoQuotationService;

    @Operation(summary = "Fetch quotations for all the available cryptos")
    @GetMapping
    public ResponseEntity<List<CurrentCryptoQuotationResponseBody>> getAllQuotations() {
        return ResponseEntity.ok().body(cryptoQuotationService.getAllCurrentQuotations());
    }

    @Operation(summary = "Fetch last 24 hours quotations for a given crypto symbol")
    @GetMapping("{symbol}")
    public ResponseEntity<CryptoQuotationResponseBody> getQuotation(@PathVariable String symbol) throws SymbolNotFoundException {
        return ResponseEntity.ok().body(cryptoQuotationService.getLast24HoursQuotationFor(symbol));
    }
}
