package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.DataLoader;
import ar.edu.unq.desapp.grupog.criptop2p.ModelTestResources;
import ar.edu.unq.desapp.grupog.criptop2p.dto.LoginRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private DataLoader dataLoader;
    private String authTokenUser;

    @BeforeEach
    void setup() throws Exception {
        // Add user to database

        User user = ModelTestResources.getBasicUser1();
        dataLoader.createUser(user);

        // Generate authentication token

        LoginRequestBody loginRequestBodyUser1 = new LoginRequestBody(user.getEmail(), user.getPassword());
        authTokenUser = requestTokenFor(loginRequestBodyUser1);

    }

    @AfterEach
    void teardown() {
        dataLoader.deleteAllUsersData();
    }

    @Test
    @DisplayName("A valid user registration requests returns a created status response")
    void aValidUserRegistrationRequestTest() throws Exception {

        UserRequestBody validUserCreationRequest = generateValidUserRequest();

        mvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(validUserCreationRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("An invalid user registration requests returns a bad request status response")
    void anInvalidUserRegistrationRequestTest() throws Exception {

        UserRequestBody invalidUserCreationRequest = generateInvalidUserRequest();

        mvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidUserCreationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email is required")));
    }

    @Test
    @DisplayName("Getting the summary of all the users of the platform should return a status code of 200 when the user is authenticated")
    void gettingSummaryOfAllUsersShouldReturnA200StatusWhenAuthenticatedTest() throws Exception {
        mvc.perform(get("/api/user/all")
                        .header(HttpHeaders.AUTHORIZATION, authTokenUser))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting the summary of all the users of the platform should return a status code of 403 when the user is not authenticated")
    void gettingSummaryOfAllUsersWithoutAuthenticationShouldReturnA403StatusTest() throws Exception {
        mvc.perform(get("/api/user/all")).andExpect(status().isForbidden());
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

    private UserRequestBody generateValidUserRequest() {
        return new UserRequestBody(
                "John",
                "Doe",
                "jdoe@domain.com",
                "Password@1",
                "some address",
                "4608738591410700747451",
                "45821674"
        );
    }

    private UserRequestBody generateInvalidUserRequest() {
        return new UserRequestBody(
                "John",
                "Doe",
                null,
                "Password@1",
                "some address",
                "4608738591410700747451",
                "45821674"
        );
    }
}
