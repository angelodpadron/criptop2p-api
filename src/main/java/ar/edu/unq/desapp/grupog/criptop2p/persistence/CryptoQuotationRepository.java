package ar.edu.unq.desapp.grupog.criptop2p.persistence;

import ar.edu.unq.desapp.grupog.criptop2p.model.CryptoQuotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoQuotationRepository extends JpaRepository<CryptoQuotation, Long> {
}
