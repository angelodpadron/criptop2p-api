package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/create")
    @ResponseBody
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User userCreated = userService.create(user);
        return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
    }

}
