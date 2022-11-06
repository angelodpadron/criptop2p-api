package ar.edu.unq.desapp.grupog.criptop2p.persistence;

import ar.edu.unq.desapp.grupog.criptop2p.model.CryptoQuotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CryptoQuotationRepository extends JpaRepository<CryptoQuotation, Long> {
    Optional<CryptoQuotation> findCryptoQuotationBySymbol(String symbol);
}
