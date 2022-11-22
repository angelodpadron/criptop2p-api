package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.DataLoader;
import ar.edu.unq.desapp.grupog.criptop2p.ModelTestResources;
import ar.edu.unq.desapp.grupog.criptop2p.dto.LoginRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.MarketOrderResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.model.OperationType;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MarketOrderIntegrationTest {

    private final String baseUrl = "/api/marketorders";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private DataLoader dataLoader;
    @Autowired
    private ObjectMapper mapper;
    private String authTokenUser1;
    private String authTokenUser2;

    @BeforeEach
    void setup() throws Exception {
        // Load user into database

        User user1 = ModelTestResources.getBasicUser1();
        User user2 = ModelTestResources.getBasicUser2();
        dataLoader.createUser(user1);
        dataLoader.createUser(user2);

        // Generate authentication tokens

        LoginRequestBody loginRequestBodyUser1 = new LoginRequestBody(user1.getEmail(), user1.getPassword());
        authTokenUser1 = requestTokenFor(loginRequestBodyUser1);

        LoginRequestBody loginRequestBodyUser2 = new LoginRequestBody(user2.getEmail(), user2.getPassword());
        authTokenUser2 = requestTokenFor(loginRequestBodyUser2);

    }

    @AfterEach
    void teardown() {
        dataLoader.deleteAllUsersData();
        dataLoader.deleteAllQuotationsData();
    }

    @Test
    @DisplayName("Getting all market orders should return a status code of 200")
    void gettingAllMarketOrdersTest() throws Exception {
        String resourcePath = "/all";
        mvc.perform(get(baseUrl + resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, authTokenUser1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting market orders created by the user should return a status code of 200")
    void gettingUserCreatedMarketOrdersTest() throws Exception {
        String resourcePath = "/created";
        mvc.perform(get(baseUrl + resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, authTokenUser1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Creating a selling market order with a valid target price should return a status code of 201")
    void creatingASellingMarketOrderWithAValidTargetPriceShouldReturnAStatusCodeOf200Test() throws Exception {
        String resourcePath = baseUrl + "/create";
        String cryptoSymbol = "BTC";
        dataLoader.createQuotationData(cryptoSymbol, 10.0, 10.0);
        MarketOrderRequestBody marketOrderRequestBody = new MarketOrderRequestBody(cryptoSymbol, 1.0, 10.0, OperationType.SELL);

        mvc.perform(post(resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(marketOrderRequestBody)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Creating selling market order with an invalid target price should return a status code of 400")
    void creatingASellingMarketOrderWithAnInvalidTargetPriceShouldReturnAStatusCodeOf400Test() throws Exception {
        String resourcePath = baseUrl + "/create";
        String cryptoSymbol = "BTC";
        dataLoader.createQuotationData(cryptoSymbol, 10.0, 10.0);
        MarketOrderRequestBody marketOrderRequestBody = new MarketOrderRequestBody(cryptoSymbol, 1.0, 10.0 * 10, OperationType.SELL);

        mvc.perform(post(resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(marketOrderRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating a selling market order with a valid target price should return a status code of 201")
    void creatingAPurchaseMarketOrderWithAValidTargetPriceShouldReturnAStatusCodeOf200Test() throws Exception {
        String resourcePath = baseUrl + "/create";
        String cryptoSymbol = "BTC";
        dataLoader.createQuotationData(cryptoSymbol, 10.0, 10.0);
        MarketOrderRequestBody marketOrderRequestBody = new MarketOrderRequestBody(cryptoSymbol, 1.0, 10.0, OperationType.PURCHASE);

        mvc.perform(post(resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(marketOrderRequestBody)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Creating selling market order with an invalid target price should return a status code of 400")
    void creatingAPurchaseMarketOrderWithAnInvalidTargetPriceShouldReturnAStatusCodeOf400Test() throws Exception {
        String resourcePath = baseUrl + "/create";
        String cryptoSymbol = "BTC";
        dataLoader.createQuotationData(cryptoSymbol, 10.0, 10.0);
        MarketOrderRequestBody marketOrderRequestBody = new MarketOrderRequestBody(cryptoSymbol, 1.0, 10.0 * 10, OperationType.PURCHASE);

        mvc.perform(post(resourcePath)
                        .header(HttpHeaders.AUTHORIZATION, authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(marketOrderRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Applying to an owned market order should return a status code of 400")
    void applyingToAnOwnedMarketOrderShouldReturnAStatusCodeOf400Test() throws Exception {
        String createResourcePath = baseUrl + "/create";
        String cryptoSymbol = "BTC";
        dataLoader.createQuotationData(cryptoSymbol, 10.0, 10.0);
        MarketOrderRequestBody marketOrderRequestBody = new MarketOrderRequestBody(cryptoSymbol, 1.0, 10.0, OperationType.SELL);

        mvc.perform(post(createResourcePath)
                        .header(HttpHeaders.AUTHORIZATION, authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(marketOrderRequestBody)))
                .andExpect(status().isCreated())
                .andDo(result -> {
                    Long createdMarketOrderId = mapper
                            .readValue(result.getResponse().getContentAsString(), MarketOrderResponseBody.class)
                            .getId();
                    String applyResourcePath = baseUrl + "/apply/" + createdMarketOrderId;
                    mvc.perform(post(applyResourcePath)
                                    .header(HttpHeaders.AUTHORIZATION, authTokenUser1))
                            .andExpect(status().isBadRequest());
                });

    }

    @Test
    @DisplayName("Applying to another user market order should return a status code of 200")
    void applyingToAnotherUserMarketOrderShouldReturnAStatusCodeOf200Test() throws Exception {
        String createResourcePath = baseUrl + "/create";

        String cryptoSymbol = "BTC";
        dataLoader.createQuotationData(cryptoSymbol, 10.0, 10.0);
        MarketOrderRequestBody marketOrderRequestBody = new MarketOrderRequestBody(cryptoSymbol, 1.0, 10.0, OperationType.SELL);

        mvc.perform(post(createResourcePath)
                        .header(HttpHeaders.AUTHORIZATION, authTokenUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(marketOrderRequestBody)))
                .andExpect(status().isCreated())
                .andDo(result -> {
                    Long createdMarketOrderId = mapper
                            .readValue(result.getResponse().getContentAsString(), MarketOrderResponseBody.class)
                            .getId();
                    String applyResourcePath = baseUrl + "/apply/" + createdMarketOrderId;
                    mvc.perform(post(applyResourcePath)
                                    .header(HttpHeaders.AUTHORIZATION, authTokenUser2))
                            .andExpect(status().isOk());
                });


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
