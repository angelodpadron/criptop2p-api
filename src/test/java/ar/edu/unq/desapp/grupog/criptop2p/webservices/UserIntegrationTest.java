package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("A valid user registration requests returns a created status response")
    public void aValidUserRegistrationRequestTest() throws Exception {

        UserRequestBody validUserCreationRequest = generateValidUserRequest();

        mvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(validUserCreationRequest)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("An invalid user registration requests returns a bad request status response")
    public void anInvalidUserRegistrationRequestTest() throws Exception {

        UserRequestBody invalidUserCreationRequest = generateInvalidUserRequest();

        mvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidUserCreationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email is required")));

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
