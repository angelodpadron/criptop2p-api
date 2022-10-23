package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.service.MarketOrderService;
import ar.edu.unq.desapp.grupog.criptop2p.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final MarketOrderService marketOrderService;

    @Operation(summary = "Create a new user on the platform")
    @PostMapping(path = "/register")
    @ResponseBody
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequestBody userRequestBody) throws EmailAlreadyTakenException {
        User userCreated = userService.saveUser(userRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @Operation(summary = "Get all market orders created")
    @GetMapping("/marketorder/all")
    @ResponseBody
    public ResponseEntity<List<MarketOrderResponseBody>> getAllUserMarketOrders() {
        return ResponseEntity.ok().body(marketOrderService.getUserMarketOrders());
    }

    @Operation(summary = "Get all user transaction orders")
    @GetMapping("/transactions/all")
    @ResponseBody
    public ResponseEntity<List<TransactionOrderResponseBody>> getAllUserTransactionOrders() {
        return ResponseEntity.ok().body(userService.getAllTransactionOrders());
    }

}
