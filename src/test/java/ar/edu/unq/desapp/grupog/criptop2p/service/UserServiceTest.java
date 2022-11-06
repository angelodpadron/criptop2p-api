package ar.edu.unq.desapp.grupog.criptop2p.service;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.exception.user.EmailAlreadyTakenException;
import ar.edu.unq.desapp.grupog.criptop2p.model.Role;
import ar.edu.unq.desapp.grupog.criptop2p.model.User;
import ar.edu.unq.desapp.grupog.criptop2p.persistence.RoleRepository;
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

import static ar.edu.unq.desapp.grupog.criptop2p.model.resources.ModelTestResources.getBasicUser1;
import static ar.edu.unq.desapp.grupog.criptop2p.model.resources.ServiceTestResources.getUserRequestBodyFromEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
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

        User savedUser = userService.saveUser(userRequestBody1);

        verify(userRepository).save(user1);
        assertEquals(user1, savedUser);
    }

    @DisplayName("Cannot save a user with an already registered email address")
    @Test
    void savingAUserWithAnAlreadyTakenEmailAddressExceptionTest() {
        when(userRepository.findByEmail(any())).thenReturn(user1);

        assertThrows(EmailAlreadyTakenException.class, () -> userService.saveUser(userRequestBody1));
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

    @DisplayName("When a role is saved the saved role is returned")
    @Test
    void savingARoleTest() {
        Role role = new Role(null, "USER_ROLE");
        when(roleRepository.save(any())).thenReturn(role);

        Role savedRole = userService.saveRole(role);

        verify(roleRepository).save(role);
        assertEquals(role, savedRole);
    }

    @DisplayName("A role can be assigned to a user")
    @Test
    void assigningARoleToAUserTest() {
        Role role = new Role(null, "USER_ROLE");
        when(userRepository.findByEmail(any())).thenReturn(user1);
        when(roleRepository.findByName(any())).thenReturn(role);

        userService.addRoleToUser(user1.getEmail(), role.getName());

        assertTrue(user1.getRoles().contains(role));

    }


}
