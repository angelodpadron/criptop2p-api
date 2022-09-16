package ar.edu.unq.desapp.grupog.criptop2p.webservices;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void aValidUserRegistrationRequestReturnsACreatedResponse() throws Exception {

        UserDTO validUserCreationRequest = generateValidUserDTO();

        mvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(validUserCreationRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void anInvalidUserRegistrationRequestReturnsABadRequestResponse() throws Exception {

        UserDTO invalidUserCreationRequest = generateInvalidUserDTO();

        mvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidUserCreationRequest)))
                .andExpect(status().isBadRequest());

    }

    private UserDTO generateValidUserDTO() {
        return new UserDTO(
                "John",
                "Doe",
                "jdoe@domain.com",
                "Password@1",
                "some address",
                "4608738591410700747451",
                "45821674"
                );
    }

    private UserDTO generateInvalidUserDTO() {
        return new UserDTO(
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
