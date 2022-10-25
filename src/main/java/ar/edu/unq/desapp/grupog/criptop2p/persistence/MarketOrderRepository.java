package ar.edu.unq.desapp.grupog.criptop2p.persistence;

import ar.edu.unq.desapp.grupog.criptop2p.model.MarketOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketOrderRepository extends JpaRepository<MarketOrder, Long> {
}
