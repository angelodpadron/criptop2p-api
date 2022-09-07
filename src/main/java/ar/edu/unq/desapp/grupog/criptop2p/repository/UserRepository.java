package ar.edu.unq.desapp.grupog.criptop2p.repository;

import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
