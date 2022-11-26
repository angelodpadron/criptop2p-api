package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.dto.UserSummaryResponseBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.user.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static ar.edu.unq.desapp.grupog.criptop2p.ModelTestResources.getBasicUser1;
import static ar.edu.unq.desapp.grupog.criptop2p.ServiceTestResources.getUserRequestBodyFromEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    @InjectMocks
    private UserService userService;
    private User user1;
    private UserRequestBody userRequestBody1;

    @BeforeEach
    void setUp() {
        user1 = getBasicUser1();
        userRequestBody1 = getUserRequestBodyFromEntity(user1);
    }

    @DisplayName("When a user is saved the saved user is returned")
    @Test
    void savingAUserTest() throws EmailAlreadyTakenException {
        when(userRepository.save(any())).thenReturn(user1);
        when(modelMapper.map(userRequestBody1, User.class)).thenReturn(user1);

        userService.saveUser(userRequestBody1);

        verify(userRepository).save(user1);
    }

    @DisplayName("Cannot save a user with an already registered email address")
    @Test
    void savingAUserWithAnAlreadyTakenEmailAddressExceptionTest() {
        when(userRepository.findByEmail(any())).thenReturn(user1);

        assertThrows(EmailAlreadyTakenException.class, () -> userService.saveUser(userRequestBody1));
    }

    @DisplayName("A summary of all the registered users can be generated")
    @Test
    void getUsersSummaryTest() {
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        when(user1.getFirstname()).thenReturn("John");
        when(user2.getFirstname()).thenReturn("Maria");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserSummaryResponseBody> responseBodies = userService.getSummaryOfAllUsers();

        assertFalse(responseBodies.isEmpty());
        assertEquals(user1.getFirstname(), responseBodies.get(0).getFirstName());
        assertEquals(user2.getFirstname(), responseBodies.get(1).getFirstName());


    }

    @DisplayName("[SPRING SECURITY] A saved user can be retrieved by email address")
    @Test
    void retrievingAUserWithTheEmailAddressTest() {
        when(userRepository.findByEmail(any())).thenReturn(user1);

        UserDetails userDetails = userService.loadUserByUsername(user1.getEmail());

        verify(userRepository).findByEmail(any());
        assertEquals(user1.getEmail(), userDetails.getUsername());

    }

    @DisplayName("[SPRING SECURITY] When a user cannot be found by its email, an exception is thrown")
    @Test
    void retrievingAnUnregisteredUserExceptionTest() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(user1.getEmail()));
    }


}
