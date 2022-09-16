package ar.edu.unq.desapp.grupog.criptop2p.service;

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

    public User create(User user) {
        log.info("Saving new user {} to the database", user.getEmail());
        return repository.save(user);
    }


}
