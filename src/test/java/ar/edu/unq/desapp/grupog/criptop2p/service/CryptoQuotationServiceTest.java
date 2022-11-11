package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.CryptoQuotationResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.CurrentCryptoQuotationResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.RawCryptoQuotationResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.cryptoquotation.SymbolNotFoundException;
import ar.edu.unq.desapp.grupog.criptop2p.model.CryptoQuotation;
import ar.edu.unq.desapp.grupog.criptop2p.model.QuotationData;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.CryptoQuotationRepository;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.QuotationDataRepository;
import ar.edu.unq.desapp.grupog.criptop2p.utils.resources.BCRAClient;
import ar.edu.unq.desapp.grupog.criptop2p.utils.resources.BinanceClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoQuotationServiceTest {
    @Mock
    private CryptoQuotationRepository cryptoQuotationRepository;
    @Mock
    private QuotationDataRepository quotationDataRepository;
    @Mock
    private BinanceClient binanceClient;
    @Mock
    private BCRAClient bcraClient;

    @Autowired
    @InjectMocks
    private CryptoQuotationService cryptoQuotationService;

    @DisplayName("The service can retrieve all current quotations locally saved")
    @Test
    void retrieveAllLocalQuotationsTest() {
        CryptoQuotation cryptoQuotation = mock(CryptoQuotation.class);
        QuotationData quotationData = mock(QuotationData.class);
        String symbol = "ALICEUSDT";

        when(cryptoQuotationRepository.findAll()).thenReturn(List.of(cryptoQuotation));
        when(cryptoQuotation.getLastQuotation()).thenReturn(quotationData);
        when(cryptoQuotation.getSymbol()).thenReturn(symbol);

        List<CurrentCryptoQuotationResponseBody> responseBodies = cryptoQuotationService.getAllCurrentQuotations();

        assertFalse(responseBodies.isEmpty());
        assertEquals(symbol, responseBodies.get(0).getSymbol());

    }

    @DisplayName("The service can retrieve the last 24 hours quotations of a cryptocurrency")
    @Test
    void retrieveLast24HoursQuotationsTest() throws SymbolNotFoundException {
        CryptoQuotation cryptoQuotation = mock(CryptoQuotation.class);
        QuotationData quotationData = mock(QuotationData.class);
        String symbol = "ALICEUSDT";

        when(cryptoQuotationRepository.findCryptoQuotationBySymbol(symbol)).thenReturn(Optional.ofNullable(cryptoQuotation));
        when(cryptoQuotation.getQuotationData()).thenReturn(List.of(quotationData));
        when(cryptoQuotation.getSymbol()).thenReturn(symbol);
        when(quotationData.getPriceInUSD()).thenReturn(0.0);
        when(quotationData.getPriceInARS()).thenReturn(0.0);


        CryptoQuotationResponseBody responseBody = cryptoQuotationService.getLast24HoursQuotationFor(symbol);

        verify(cryptoQuotationRepository).findCryptoQuotationBySymbol(symbol);
        assertEquals(symbol, responseBody.getSymbol());
        assertFalse(responseBody.getQuotations().isEmpty());
        assertEquals(0.0, responseBody.getQuotations().get(0).getPriceInUsd());
        assertEquals(0.0, responseBody.getQuotations().get(0).getPriceInArs());

    }

    @DisplayName("The service cannot retrieve quotations data of an invalid symbol")
    @Test
    void retrieveLast24HoursQuotationsExceptionTest() {
        String symbol = "ALICEUSDT";

        assertThrows(SymbolNotFoundException.class,
                () -> cryptoQuotationService.getLast24HoursQuotationFor(symbol));
    }

    @DisplayName("The service can retrieve the last usd price of a given cryptocurrency")
    @Test
    void retrieveLastUsdPriceTest() throws SymbolNotFoundException {
        CryptoQuotation cryptoQuotation = mock(CryptoQuotation.class);
        QuotationData quotationData = mock(QuotationData.class);
        String symbol = "ALICEUSDT";

        when(cryptoQuotationRepository.findCryptoQuotationBySymbol(symbol)).thenReturn(Optional.ofNullable(cryptoQuotation));
        when(cryptoQuotation.getLastQuotation()).thenReturn(quotationData);
        when(quotationData.getPriceInUSD()).thenReturn(0.0);

        Double retrievedPrice = cryptoQuotationService.getCurrentUsdPriceFor(symbol);

        assertEquals(0.0, retrievedPrice);

    }

    @DisplayName("The service can update local cryptocurrency quotations")
    @Test
    void updateExistingCryptoQuotationsTest() {
        String symbol = "ALICEUSDT";

        CryptoQuotation cryptoQuotation = mock(CryptoQuotation.class);
        RawCryptoQuotationResponseBody rawQuotation = mock(RawCryptoQuotationResponseBody.class);
        when(rawQuotation.getSymbol()).thenReturn(symbol);

        when(binanceClient.getAllQuotations()).thenReturn(List.of(rawQuotation));
        when(bcraClient.getLastUSDARSQuotation()).thenReturn(0.0);
        when(cryptoQuotationRepository.findCryptoQuotationBySymbol(symbol)).thenReturn(Optional.ofNullable(cryptoQuotation));

        cryptoQuotationService.updateLocalQuotations();

        verify(cryptoQuotationRepository).save(cryptoQuotation);
        verify(quotationDataRepository).save(any());
        verify(cryptoQuotation).addQuotationData(any());

    }

    @DisplayName("A new cryptocurrency is created when updated local quotations for the first time")
    @Test
    void createCryptoQuotationTest() {
        String symbol = "ALICEUSDT";

        RawCryptoQuotationResponseBody rawQuotation = mock(RawCryptoQuotationResponseBody.class);
        when(rawQuotation.getSymbol()).thenReturn(symbol);

        when(binanceClient.getAllQuotations()).thenReturn(List.of(rawQuotation));
        when(bcraClient.getLastUSDARSQuotation()).thenReturn(0.0);

        cryptoQuotationService.updateLocalQuotations();

        verify(cryptoQuotationRepository).save(any());
        verify(quotationDataRepository).save(any());
    }


}
