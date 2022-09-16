package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserDTO;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping(path = "/create")
    @ResponseBody
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        User userCreated = userService.create(toUserEntity(userDTO));
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/create").toUriString());
        return ResponseEntity.created(uri).body(userCreated);
    }

    private User toUserEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

}
