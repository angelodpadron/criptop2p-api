package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.DataLoader;
import ar.edu.unq.desapp.grupog.criptop2p.dto.CryptoQuotationResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.utils.resources.BCRAClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class CryptoQuotationIntegrationTest {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String baseUrl = "/api/quotations/";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private DataLoader dataLoader;

    @MockBean
    private BCRAClient bcraClient;

    @BeforeEach
    void setup() {
        Mockito.when(bcraClient.getLastUSDARSQuotation()).thenReturn(0.0);
    }

    @Test
    @DisplayName("Getting all saved quotations should return a status code of 200")
    void gettingAllSavedQuotationTest() throws Exception {
        mvc.perform(get(baseUrl)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting the quotations of an existing crypto-asset should return a status code of 200")
    void gettingLast24HoursCryptoQuotationsTest() throws Exception {
        String cryptoSymbol = "SYMBOL";
        dataLoader.createQuotationData(cryptoSymbol, 10.0, 10.0);

        MvcResult response = mvc.perform(get(baseUrl + cryptoSymbol)).andReturn();
        CryptoQuotationResponseBody dto = mapper.readValue(response.getResponse().getContentAsString(), CryptoQuotationResponseBody.class);

        assertEquals(cryptoSymbol, dto.getSymbol());
        assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
    }

    @Test
    @DisplayName("Getting the quotations of an non existing crypto-asset should return a status code of 404")
    void gettingLast24HoursOfANonExistingCryptoTest() throws Exception {
        String invalidCryptoSymbol = "PESOS";
        mvc.perform(get(baseUrl + invalidCryptoSymbol)).andExpect(status().isBadRequest());
    }

}
