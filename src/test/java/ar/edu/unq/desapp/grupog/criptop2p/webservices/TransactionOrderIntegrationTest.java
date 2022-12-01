package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.DataLoader;
import ar.edu.unq.desapp.grupog.criptop2p.ModelTestResources;
import ar.edu.unq.desapp.grupog.criptop2p.dto.LoginRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.TransactionOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.model.OperationType;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.utils.resources.BCRAClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionOrderIntegrationTest {

    private final String baseUrl = "/api/transactions";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private DataLoader dataLoader;
    @MockBean
    private BCRAClient bcraClient;
    private String dealerUserAuthToken;
    private String interestedUserAuthToken;
    private String thirdPartyUserAuthToken;
    private Long currentTransactionOrderId;
    private Long currentMarketOrderId;

    @BeforeEach
    void setup() throws Exception {

        // Config required component mock
        Mockito.when(bcraClient.getLastUSDARSQuotation()).thenReturn(0.0);

        User dealerUser = ModelTestResources.getBasicUser1();
        User interestedUser = ModelTestResources.getBasicUser2();
        User thirdPartyUser = ModelTestResources.getBasicUser3();

        dataLoader.createUser(dealerUser);
        dataLoader.createUser(interestedUser);
        dataLoader.createUser(thirdPartyUser);

        dealerUserAuthToken = requestTokenFor(new LoginRequestBody(dealerUser.getEmail(), dealerUser.getPassword()));
        interestedUserAuthToken = requestTokenFor(new LoginRequestBody(interestedUser.getEmail(), interestedUser.getPassword()));
        thirdPartyUserAuthToken = requestTokenFor(new LoginRequestBody(thirdPartyUser.getEmail(), thirdPartyUser.getPassword()));

        dataLoader.createQuotationData("BTC", 10.0, 10.0);
        MarketOrderRequestBody sellingMarketOrderRequestBody = new MarketOrderRequestBody("BTC", 10.0, 10.0, OperationType.SELL);

        String createMarketOrderResourcePath = "/api/marketorders/create";

        mvc.perform(post(createMarketOrderResourcePath)
                        .header(HttpHeaders.AUTHORIZATION, dealerUserAuthToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sellingMarketOrderRequestBody)))
                .andExpect(status().isCreated())
                .andDo(result -> currentMarketOrderId = mapper
                        .readValue(result.getResponse().getContentAsString(), MarketOrderResponseBody.class)
                        .getId());

        String applyToMarketOrderResourcePath = "/api/marketorders/apply/" + currentMarketOrderId;

        mvc.perform(post(applyToMarketOrderResourcePath)
                        .header(HttpHeaders.AUTHORIZATION, interestedUserAuthToken))
                .andExpect(status().isOk())
                .andDo(result -> currentTransactionOrderId = mapper
                        .readValue(result.getResponse().getContentAsString(), TransactionOrderResponseBody.class)
                        .getTransactionOrderId());

    }

    @AfterEach
    void teardown() {
        dataLoader.deleteAllUsersData();
        dataLoader.deleteAllQuotationsData();
    }

    @Test
    @DisplayName("Getting the transaction orders from the requesting user should return a status code of 200")
    void gettingTheTransactionOrdersFromTheUserShouldReturnStatusCodeOf200Test() throws Exception {
        String resourcePath = baseUrl + "/all";

        mvc.perform(get(resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, dealerUserAuthToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting the volume of cryptos operated by the user between two dates should return a status code of 200")
    void gettingTheVolumeOfCryptosOperatedShouldReturnStatusCode200Test() throws Exception {
        String yesterday = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME);
        String today = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        String resourcePath = baseUrl + "/summary";

        mvc.perform(get(resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, dealerUserAuthToken)
                        .param("from", yesterday)
                        .param("to", today))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Cancelling a transaction order as one of the party users should return a status code of 200")
    void cancellingATransactionOrderShouldReturnStatusCode200Test() throws Exception {
        String resourcePath = baseUrl + "/cancel/" + currentTransactionOrderId;

        mvc.perform(post(resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, interestedUserAuthToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("CANCELLED")));
    }

    @Test
    @DisplayName("Cancelling a transaction order as a third party user should return a status code of 400")
    void cancellingATransactionOrderAsThirdPartyShouldReturnStatusCode400Test() throws Exception {
        String resourcePath = baseUrl + "/cancel/" + currentTransactionOrderId;

        mvc.perform(post(resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, thirdPartyUserAuthToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Transaction cannot be operated by unrelated users")));

    }

    @Test
    @DisplayName("Notifying the transference of a transaction order as the corresponding party user should return a status code of 200")
    void notifyingTransactionOrderTransferenceAsCorrespondingPartyUserShouldReturnStatusCode200Test() throws Exception {
        String resourcePath = baseUrl + "/transfer/" + currentTransactionOrderId;

        mvc.perform(post(resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, interestedUserAuthToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("AWAITING_RECEPTION")));
    }

    @Test
    @DisplayName("Notifying the reception of the transference of a transaction order as the corresponding party user should return a status code of 200")
    void notifyingTransactionOrderReceptionAsCorrespondingPartyUserShouldReturnStatusCode200Test() throws Exception {
        String performTransferenceResourcePath = baseUrl + "/transfer/" + currentTransactionOrderId;
        mvc.perform(post(performTransferenceResourcePath)
                        .header(HttpHeaders.AUTHORIZATION, interestedUserAuthToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("AWAITING_RECEPTION")));

        String confirmReceptionResourcePath = baseUrl + "/confirm/" + currentTransactionOrderId;
        mvc.perform(post(confirmReceptionResourcePath)
                        .header(HttpHeaders.AUTHORIZATION, dealerUserAuthToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("CLOSED")));

    }

    @Test
    @DisplayName("Operating a closed transaction order by any of the party users should return a status code of 400")
    void operatingAClosedTransactionOrderByAnyPartyUserShouldReturnStatusCode400Test() throws Exception {
        String performTransferenceResourcePath = baseUrl + "/transfer/" + currentTransactionOrderId;

        mvc.perform(post(performTransferenceResourcePath)
                        .header(HttpHeaders.AUTHORIZATION, interestedUserAuthToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("AWAITING_RECEPTION")));

        String confirmReceptionResourcePath = baseUrl + "/confirm/" + currentTransactionOrderId;

        mvc.perform(post(confirmReceptionResourcePath)
                        .header(HttpHeaders.AUTHORIZATION, dealerUserAuthToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("CLOSED")));

        // At this point, the transaction order is already closed

        String cancelTransactionOrderResourcePath = baseUrl + "/cancel/" + currentTransactionOrderId;

        mvc.perform(post(cancelTransactionOrderResourcePath)
                        .header(HttpHeaders.AUTHORIZATION, dealerUserAuthToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("The transaction has been closed and cannot be processed")));

    }


    private String requestTokenFor(LoginRequestBody loginRequestBody) throws Exception {
        String loginResourcePath = "/api/user/login";

        return "Bearer " + mvc.perform(
                        post(loginResourcePath)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(loginRequestBody)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(HttpHeaders.AUTHORIZATION);
    }

}
