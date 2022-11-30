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
import ar.edu.unq.desapp.grupog.criptop2p.utils.resources.Mappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CryptoQuotationService {

    private final CryptoQuotationRepository cryptoQuotationRepository;
    private final QuotationDataRepository quotationDataRepository;
    private final BinanceClient binanceClient;
    private final BCRAClient bcraClient;

    @Cacheable("all_quotations")
    public List<CurrentCryptoQuotationResponseBody> getAllCurrentQuotations() {
        List<CryptoQuotation> cryptoQuotations = cryptoQuotationRepository.findAll();
        return cryptoQuotations.stream().map(Mappers::cryptoQuotationEntityToResponseBody).toList();
    }

    @Cacheable("last_24h_quotation")
    public CryptoQuotationResponseBody getLast24HoursQuotationFor(String symbol) throws SymbolNotFoundException {
        CryptoQuotation cryptoQuotation = getCryptoQuotation(symbol);
        return Mappers.cryptoQuotationEntityTo24HoursResponseBody(cryptoQuotation);
    }

    public Double getCurrentUsdPriceFor(String symbol) throws SymbolNotFoundException {
        CryptoQuotation cryptoQuotation = getCryptoQuotation(symbol);
        return cryptoQuotation.getLastQuotation().getPriceInUSD();
    }

    private CryptoQuotation getCryptoQuotation(String symbol) throws SymbolNotFoundException {
        return cryptoQuotationRepository
                .findCryptoQuotationBySymbol(symbol)
                .orElseThrow(() -> new SymbolNotFoundException(symbol));
    }

    public void updateLocalQuotations() {
        List<RawCryptoQuotationResponseBody> rawQuotations = binanceClient.getAllQuotations();
        Double usdExchange = bcraClient.getLastUSDARSQuotation();

        rawQuotations.forEach(rawQuotation -> {
            Optional<CryptoQuotation> optionalCryptoQuotation = cryptoQuotationRepository.findCryptoQuotationBySymbol(rawQuotation.getSymbol());

            CryptoQuotation cryptoQuotation = optionalCryptoQuotation.orElseGet(() -> new CryptoQuotation(rawQuotation.getSymbol(), new ArrayList<>()));
            cryptoQuotationRepository.save(cryptoQuotation);

            QuotationData quotationData = new QuotationData(cryptoQuotation, rawQuotation.getPrice(), rawQuotation.getPrice() * usdExchange);
            quotationDataRepository.save(quotationData);

            cryptoQuotation.addQuotationData(quotationData);
        });
    }


}
