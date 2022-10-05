package ar.edu.unq.desapp.grupog.criptop2p.service.resources;

import ar.edu.unq.desapp.grupog.criptop2p.dto.USDQuotationResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BCRAClient {

    private final RestTemplate restTemplate;


    public Double getLastUSDARSQuotation() {

        // Configure headers with authentication token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Resources.BCRA_TOKEN);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        // Get the last quotation from the USD to ARS exchange rate list
        Optional<List<USDQuotationResponseBody>> usdQuotations = Optional.ofNullable(restTemplate
                .exchange(
                        Resources.BCRA_USD_QUOTATION_URL,
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<List<USDQuotationResponseBody>>() {
                        }
                ).getBody());

        // Exchange method may return null
        if (usdQuotations.isPresent()) {
            List<USDQuotationResponseBody> quotationList = usdQuotations.get();
            if (quotationList.size() > 0) {
                return quotationList.get(quotationList.size() - 1).getValue();
            }
        }

        return 0.0;

    }

}
