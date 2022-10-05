package ar.edu.unq.desapp.grupog.criptop2p.service.resources;

import ar.edu.unq.desapp.grupog.criptop2p.dto.CryptoQuotationResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BinanceClient {

    private final RestTemplate restTemplate;

    public List<CryptoQuotationResponseBody> getAllQuotations() {
        Optional<List<CryptoQuotationResponseBody>> quotations = Optional.ofNullable(
                restTemplate.exchange(
                        getAllQuotationsUrl(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<CryptoQuotationResponseBody>>() {
                        }
                ).getBody()
        );

        return quotations.orElseGet(ArrayList::new);

    }

    public CryptoQuotationResponseBody getQuotation(String symbol) {

        return restTemplate.exchange(
                getQuotationUrl(symbol),
                HttpMethod.GET,
                null,
                CryptoQuotationResponseBody.class
        ).getBody();
    }

    private URI getAllQuotationsUrl() {
        return UriComponentsBuilder
                .fromHttpUrl(Resources.BINANCE_ALL_QUOTATIONS_URL)
                .replaceQueryParam("symbols", getFormattedSymbols())
                .build()
                .toUri();
    }

    private URI getQuotationUrl(String symbol) {
        return UriComponentsBuilder
                .fromHttpUrl(Resources.BINANCE_QUOTATION_URL)
                .replaceQueryParam("symbol", symbol)
                .build()
                .toUri();
    }

    private String getFormattedSymbols() {
        return Resources.CRYPTO_SYMBOLS
                .stream()
                .map(n -> "\"" + n + "\"")
                .collect(Collectors.joining(",", "[", "]"));
    }


}
