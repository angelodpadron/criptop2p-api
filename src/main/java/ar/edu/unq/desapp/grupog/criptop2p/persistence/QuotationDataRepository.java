package ar.edu.unq.desapp.grupog.criptop2p.persistence;

import ar.edu.unq.desapp.grupog.criptop2p.model.QuotationData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationDataRepository extends JpaRepository<QuotationData, Long> {
}
