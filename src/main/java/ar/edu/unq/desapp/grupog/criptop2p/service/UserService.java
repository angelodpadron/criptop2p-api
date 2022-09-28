package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    public User create(UserRequestBody userRequestBody) throws EmailAlreadyTakenException {
        checkIfRequestedEmailIsAlreadyTaken(userRequestBody);
        User user = requestBodyToEntityMapper(userRequestBody);
        log.info("Saving new user {} to the database", user.getEmail());

        return repository.save(user);
    }


    private User requestBodyToEntityMapper(UserRequestBody requestBody) {
        User user = modelMapper.map(requestBody, User.class);
        checkUserValidations(user);
        return user;
    }

    private void checkIfRequestedEmailIsAlreadyTaken(UserRequestBody userRequestBody) throws EmailAlreadyTakenException {
        if (emailAlreadyTaken(userRequestBody.getEmail())) {
            throw new EmailAlreadyTakenException("There is an account with that email address:" + userRequestBody.getEmail());
        }
    }

    private boolean emailAlreadyTaken(String email) {
        return !repository.findByEmail(email).isEmpty();
    }

    private void checkUserValidations(User user) throws ConstraintViolationException {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }


}
