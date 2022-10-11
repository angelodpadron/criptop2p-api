package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.exception.MarketOrderException;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user on the platform")
    @PostMapping(path = "/register")
    @ResponseBody
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequestBody userRequestBody) throws EmailAlreadyTakenException {
        User userCreated = userService.saveUser(userRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @Operation(summary = "Create a market order")
    @PostMapping(path = "/marketorder/create")
    @ResponseBody
    public ResponseEntity<MarketOrderRequestBody> createMarketOrder(@RequestBody MarketOrderRequestBody marketOrderRequestBody) throws MarketOrderException {
        userService.addMarketOrderToUser(marketOrderRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(marketOrderRequestBody);
    }
}
