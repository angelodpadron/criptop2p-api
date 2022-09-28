package ar.edu.unq.desapp.grupog.criptop2p.persistence;

import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);
}
