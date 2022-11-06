package ar.edu.unq.desapp.grupog.criptop2p.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class CryptoQuotationTest {

    @DisplayName("Quotation data can be added to a crypto quotation")
    @Test
    void addQuotationDataToACryptoQuotationTest() {
        QuotationData quotationData = mock(QuotationData.class);
        CryptoQuotation cryptoQuotation = new CryptoQuotation("ALICEUSDT", new ArrayList<>());

        cryptoQuotation.addQuotationData(quotationData);

        assertTrue(cryptoQuotation.getQuotationData().contains(quotationData));
    }

    @DisplayName("The last quotation data added can be retrieved from a crypto quotation")
    @Test
    void retrieveLastQuotationDataAddedTest() {
        QuotationData quotationData1 = mock(QuotationData.class);
        QuotationData quotationData2 = mock(QuotationData.class);

        CryptoQuotation cryptoQuotation = new CryptoQuotation("ALICEUSDT", new ArrayList<>());

        cryptoQuotation.addQuotationData(quotationData1);
        cryptoQuotation.addQuotationData(quotationData2);

        assertEquals(quotationData2, cryptoQuotation.getLastQuotation());


    }


}
