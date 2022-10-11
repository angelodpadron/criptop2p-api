package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/marketorder")
@RequiredArgsConstructor
@Slf4j
public class MarketOrderController {

    private final UserService userService;

    @Operation(summary = "Get all market orders")
    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<MarketOrderResponseBody>> getMarketOrders() {
        return ResponseEntity.ok().body(userService.getMarketOrders());
    }


}
