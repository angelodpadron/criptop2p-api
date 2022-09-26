package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.exception.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;

    public User create(User user) throws EmailAlreadyTakenException {
        if (emailAlreadyTaken(user.getEmail())) {
            throw new EmailAlreadyTakenException("There is an account with that email address:" + user.getEmail());
        }
        log.info("Saving new user {} to the database", user.getEmail());
        return repository.save(user);
    }

    private boolean emailAlreadyTaken(String email) {
        return !repository.findByEmail(email).isEmpty();
    }


}
