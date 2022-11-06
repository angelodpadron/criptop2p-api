package ar.edu.unq.desapp.grupog.criptop2p.service.resources;

import ar.edu.unq.desapp.grupog.criptop2p.dto.RawCryptoQuotationResponseBody;
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

    public List<RawCryptoQuotationResponseBody> getAllQuotations() {
        Optional<List<RawCryptoQuotationResponseBody>> quotations = Optional.ofNullable(
                restTemplate.exchange(
                        getAllQuotationsUrl(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<RawCryptoQuotationResponseBody>>() {
                        }
                ).getBody()
        );

        return quotations.orElseGet(ArrayList::new);

    }

    private URI getAllQuotationsUrl() {
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


}
