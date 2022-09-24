package ar.edu.unq.desapp.grupog.criptop2p.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("The username can have three to thirty characters")
    public void usernameLengthCanHaveThreeToThirtyCharactersTest() {
        User user = generateUserWithoutFirstname();
        user.setFirstname("Joseph Joestar");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("The username cannot cannot have less than three characters")
    public void usernameCannotHaveLessThanThreeCharactersTest() {
        User user = generateUserWithoutFirstname();
        user.setFirstname("Ty");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("The username cannot have more than thirty characters")
    public void usernameCannotHaveMoreThanThirtyCharactersTest() {
        User user = generateUserWithoutFirstname();
        user.setFirstname("Wolfeschlegelsteinhausenbergerdorff");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A valid user email should not raise a validation error")
    public void userEmailIsValidTest() {
        User user = generateUserWithoutEmail();
        user.setEmail("jdoe@gmail.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("An invalid user email should raise a validation error")
    public void userEmailIsInvalidTest() {
        User user = generateUserWithoutEmail();
        user.setEmail("Obviously not an email");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user password should contain an uppercase, a lowercase, a number and a special character")
    public void userPasswordIsValidPasswordTest() {
        User user = generateUserWithoutPassword();
        user.setPassword("Password@1");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user password without an uppercase, a lowercase, a number and a special character should raise a validation error")
    public void userPasswordIsInvalidTest() {
        User user = generateUserWithoutEmail();
        user.setPassword("123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user address can have three to thirty characters")
    public void userAddressIsValidTest() {
        User user = generateUserWhitoutAddress();
        user.setAddress("Florida Avenue");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user address cannot have less than three characters")
    public void userAddressCannotHaveLessThanThreeCharactersTest() {
        User user = generateUserWhitoutAddress();
        user.setAddress("St");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user address cannot have more than thirty characters")
    public void userAddressCannotHaveMoreThanThirtyCharactersTest() {
        User user = generateUserWhitoutAddress();
        user.setAddress("Wolfeschlegelsteinhausenbergerdorff Street");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user cvu should consist of 22 digits")
    public void userCVUIsValidTest() {
        User user = generateUserWhitoutCVU();
        user.setCvuMercadoPago("4608738591410700747451");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user CVU of size other than 22 should raise an error.")
    public void userCVUIsInvalidTest() {
        User user = generateUserWhitoutCVU();
        user.setCvuMercadoPago("123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user wallet address should consist of 8 digits")
    public void userWalletAddressIsValidTest() {
        User user = generateUserWithoutWalletAddress();
        user.setWalletAddress("12345678");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user wallet address of size other than 8 should raise an error.")
    public void userWalletAddressIsInvalidTest() {
        User user = generateUserWithoutWalletAddress();
        user.setWalletAddress("123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }


    private User generateValidUser() {
        User validUser = new User();
        validUser.setFirstname("John");
        validUser.setLastname("Doe");
        validUser.setEmail("jdoe@gmail.com");
        validUser.setPassword("Password@1");
        validUser.setAddress("Florida Avenue");
        validUser.setCvuMercadoPago("4608738591410700747451");
        validUser.setWalletAddress("45821674");

        return validUser;
    }

    private User generateUserWithoutFirstname() {
        User userWithoutFirstname = generateValidUser();
        userWithoutFirstname.setFirstname(null);
        return userWithoutFirstname;
    }

    private User generateUserWithoutPassword() {
        User userWithoutPassword = generateValidUser();
        userWithoutPassword.setPassword(null);
        return userWithoutPassword;
    }

    private User generateUserWithoutEmail() {
        User userWithoutEmail = generateValidUser();
        userWithoutEmail.setEmail(null);
        return userWithoutEmail;
    }

    private User generateUserWhitoutAddress() {
        User userWithoutAddress = generateValidUser();
        userWithoutAddress.setAddress(null);
        return userWithoutAddress;
    }

    private User generateUserWhitoutCVU() {
        User userWithoutCVU = generateValidUser();
        userWithoutCVU.setCvuMercadoPago(null);
        return userWithoutCVU;
    }

    private User generateUserWithoutWalletAddress() {
        User userWhitoutWalletAddress = generateValidUser();
        userWhitoutWalletAddress.setWalletAddress(null);
        return userWhitoutWalletAddress;
    }

}
