package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.CryptoQuotationResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.USDQuotationResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.model.CryptoQuotation;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.CryptoQuotationRepository;
import ar.edu.unq.desapp.grupog.criptop2p.service.resources.Resources;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CryptoQuotationService {


    private final RestTemplate template;
    private final CryptoQuotationRepository cryptoQuotationRepository;


    public List<CryptoQuotation> getAllQuotations() {

        URI binanceAllQuotationsUrl = getBinanceAllQuotationsUrl();
        CryptoQuotationResponseBody[] binanceQuotationsResponse = template.getForObject(binanceAllQuotationsUrl, CryptoQuotationResponseBody[].class);

        return mapCryptoResponsesToCryptoQuotations(binanceQuotationsResponse);
    }


    private Double getLastUSDARSQuotation() {

        // Configure headers with authentication token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Resources.BCRA_TOKEN);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        // Get the last quotation from the USD to ARS exchange rate list
        ResponseEntity<USDQuotationResponseBody[]> bankAPIResponse = template
                .exchange(
                        Resources.BCRA_USD_QUOTATION_URL,
                        HttpMethod.GET,
                        httpEntity,
                        USDQuotationResponseBody[].class
                );

        USDQuotationResponseBody lastUSDQuotation = bankAPIResponse.getBody()[bankAPIResponse.getBody().length - 1];

        return Math.floor(lastUSDQuotation.getValue() * 100.0) / 100.0;
    }

    private URI getBinanceAllQuotationsUrl() {
        return UriComponentsBuilder
                .fromHttpUrl(Resources.BINANCE_ALL_QUOTATIONS_URL)
                .replaceQueryParam("symbols", getFormattedSymbols())
                .build()
                .toUri();
    }

    private String getFormattedSymbols() {
        return Resources.CRYPTO_SYMBOLS
                .stream()
                .map(n -> "\"" + n + "\"")
                .collect(Collectors.joining(",", "[", "]"));
    }

    private List<CryptoQuotation> mapCryptoResponsesToCryptoQuotations(CryptoQuotationResponseBody[] responseBodies) {

        List<CryptoQuotation> cryptoQuotations = new ArrayList<>();
        Double currentUSDARSQuotation = getLastUSDARSQuotation();

        for (CryptoQuotationResponseBody responseBody : responseBodies) {
            CryptoQuotation cryptoQuotation = mapCryptoResponseToCryptoQuotation(responseBody, currentUSDARSQuotation);

            cryptoQuotations.add(cryptoQuotation);

            // For now, the repository is used only to generate the id of the entities
            cryptoQuotationRepository.save(cryptoQuotation);
        }

        return cryptoQuotations;
    }

    private CryptoQuotation mapCryptoResponseToCryptoQuotation(CryptoQuotationResponseBody responseBody, Double currentUSDARSQuotation) {
        return new CryptoQuotation(
                responseBody.getSymbol(),
                responseBody.getPrice(),
                responseBody.getPrice() * currentUSDARSQuotation
        );
    }


}
