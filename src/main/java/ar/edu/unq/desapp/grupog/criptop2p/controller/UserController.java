package ar.edu.unq.desapp.grupog.criptop2p.controller;

import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @CrossOrigin
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> list() {
        List<User> users = userService.list();
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User userCreated = userService.create(user);
        return new ResponseEntity(userCreated, HttpStatus.CREATED);
    }

}
