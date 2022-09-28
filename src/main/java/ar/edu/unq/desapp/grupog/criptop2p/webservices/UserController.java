package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/create")
    @ResponseBody
    public ResponseEntity<User> createUser(@RequestBody UserRequestBody userRequestBody) throws EmailAlreadyTakenException {
        User userCreated = userService.create(userRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

}
