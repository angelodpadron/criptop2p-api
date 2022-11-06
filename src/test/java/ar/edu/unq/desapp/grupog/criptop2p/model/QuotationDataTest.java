package ar.edu.unq.desapp.grupog.criptop2p.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class QuotationDataTest {

    @DisplayName("When instantiating a quotation data class, a timestamp is created")
    @Test
    void timestampOfAQuotationDataTest() {
        CryptoQuotation cryptoQuotation = mock(CryptoQuotation.class);
        LocalDateTime beforeExecution = LocalDateTime.now().minusMinutes(1);
        QuotationData quotationData = new QuotationData(cryptoQuotation, 0.0, 0.0);

        assertTrue(beforeExecution.isBefore(quotationData.getLastUpdate()));
    }

}
