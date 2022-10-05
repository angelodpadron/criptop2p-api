package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.CryptoQuotationResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.model.CryptoQuotation;
import ar.edu.unq.desapp.grupog.criptop2p.service.resources.BCRAClient;
import ar.edu.unq.desapp.grupog.criptop2p.service.resources.BinanceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoQuotationService {


    private final BinanceClient binanceAPIClient;
    private final BCRAClient bcraClient;
//    private final CryptoQuotationRepository cryptoQuotationRepository;


    public List<CryptoQuotation> getAllQuotations() {
        return mapToCryptoQuotationList(binanceAPIClient.getAllQuotations());
    }

    public CryptoQuotation getQuotation(String symbol) {
        CryptoQuotationResponseBody cryptoQuotationResponseBody = binanceAPIClient.getQuotation(symbol);
        Double currentUSDQuotation = bcraClient.getLastUSDARSQuotation();
        return mapToCryptoQuotation(cryptoQuotationResponseBody, currentUSDQuotation);
    }


    private List<CryptoQuotation> mapToCryptoQuotationList(List<CryptoQuotationResponseBody> responseBodies) {
        List<CryptoQuotation> cryptoQuotations = new ArrayList<>();
        Double currentUSDARSQuotation = bcraClient.getLastUSDARSQuotation();

        for (CryptoQuotationResponseBody responseBody : responseBodies) {
            CryptoQuotation cryptoQuotation = mapToCryptoQuotation(responseBody, currentUSDARSQuotation);
            cryptoQuotations.add(cryptoQuotation);

            // For now, the repository is used only to generate the id of the entities
//            cryptoQuotationRepository.save(cryptoQuotation);
        }

        return cryptoQuotations;
    }

    private CryptoQuotation mapToCryptoQuotation(CryptoQuotationResponseBody responseBody, Double currentUSDARSQuotation) {
        Double arsEquivalent = round(responseBody.getPrice() * currentUSDARSQuotation);

        return new CryptoQuotation(
                responseBody.getSymbol(),
                responseBody.getPrice(),
                arsEquivalent
        );
    }

    private Double round(Double value) {
        return Math.round(value * 100.00) / 100.0;
    }


}
