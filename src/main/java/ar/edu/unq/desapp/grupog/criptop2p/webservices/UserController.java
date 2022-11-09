package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.UserSummaryResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.user.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
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

    @Operation(summary = "Create a new user on the platform")
    @PostMapping(path = "/register")
    @ResponseBody
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequestBody userRequestBody) throws EmailAlreadyTakenException {
        User userCreated = userService.saveUser(userRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @Operation(summary = "Get a summary of all the users of the platform")
    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<List<UserSummaryResponseBody>> getSummaryOfAllUsers() {
        return ResponseEntity.ok().body(userService.getSummaryOfAllUsers());
    }

}
