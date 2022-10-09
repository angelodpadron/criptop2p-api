package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.model.CryptoQuotation;
import ar.edu.unq.desapp.grupog.criptop2p.service.CryptoQuotationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/quotations")
@RequiredArgsConstructor
@Slf4j
public class CryptoQuotationsController {

    private final CryptoQuotationService cryptoQuotationService;

    @Operation(summary = "Fetch quotations for all the available cryptos")
    @GetMapping
    public ResponseEntity<List<CryptoQuotation>> getAllQuotations() {
        // user consulting: SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        return ResponseEntity.ok().body(cryptoQuotationService.getAllQuotations());
    }
}
