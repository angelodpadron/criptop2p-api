package ar.edu.unq.desapp.grupog.criptop2p.persistence;

import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionOrderRepository extends JpaRepository<TransactionOrder, Long> {
}
