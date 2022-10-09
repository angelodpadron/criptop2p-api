package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.model.Role;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.RoleRepository;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User with email {} not found", email);
            throw new UsernameNotFoundException("User not found");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));


        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }


    public User create(UserRequestBody userRequestBody) throws EmailAlreadyTakenException {
        User user = requestBodyToEntityMapper(userRequestBody);

        checkUserValidations(user);
        checkIfEmailIsAlreadyTaken(user.getEmail());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        log.info("Saving new user {} to the database", user.getEmail());

        return userRepository.save(user);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public void addRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        user.addRole(role);

    }


    private User requestBodyToEntityMapper(UserRequestBody requestBody) {
        return modelMapper.map(requestBody, User.class);
    }

    private void checkIfEmailIsAlreadyTaken(String email) throws EmailAlreadyTakenException {
        if (emailAlreadyTaken(email)) {
            throw new EmailAlreadyTakenException("There is an account with that email address:" + email);
        }
    }

    private boolean emailAlreadyTaken(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private void checkUserValidations(User user) throws ConstraintViolationException {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
