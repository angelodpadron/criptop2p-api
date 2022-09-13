package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserDTO;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.service.UserService;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(path = "/create")
    @ResponseBody
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        User userEntity = toUserEntity(userDTO);
        User userCreated = userService.create(userEntity);
        return new ResponseEntity<>(userCreated, HttpStatus.CREATED);
    }

    private User toUserEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

}
