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
        User user = generateUserWithFirstname("Joseph Joestar");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("The username cannot cannot have less than three characters")
    public void usernameCannotHaveLessThanThreeCharactersTest() {
        User user = generateUserWithFirstname("Ty");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("The username cannot have more than thirty characters")
    public void usernameCannotHaveMoreThanThirtyCharactersTest() {
        User user = generateUserWithFirstname("Wolfeschlegelsteinhausenbergerdorff");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A valid user email should not raise a validation error")
    public void userEmailIsValidTest() {
        User user = generateUserWithEmail("jdoe@gmail.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("An invalid user email should raise a validation error")
    public void userEmailIsInvalidTest() {
        User user = generateUserWithEmail("Obviously not an email");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user password should contain an uppercase, a lowercase, a number and a special character")
    public void userPasswordIsValidPasswordTest() {
        User user = generateUserWithPassword("passworD@1");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user password without an uppercase, a lowercase, a number and a special character should raise a validation error")
    public void userPasswordIsInvalidTest() {
        User user = generateUserWithEmail("123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user address can have three to thirty characters")
    public void userAddressIsValidTest() {
        User user = generateUserWhitAddress("Florida Avenue");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user address cannot have less than three characters")
    public void userAddressCannotHaveLessThanThreeCharactersTest() {
        User user = generateUserWhitAddress("St");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user address cannot have more than thirty characters")
    public void userAddressCannotHaveMoreThanThirtyCharactersTest() {
        User user = generateUserWhitAddress("Wolfeschlegelsteinhausenbergerdorff Street");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user cvu should consist of 22 digits")
    public void userCVUIsValidTest() {
        User user = generateUserWhitCVU("4608738591410700747451");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user CVU of size other than 22 should raise an error.")
    public void userCVUIsInvalidTest() {
        User user = generateUserWhitCVU("123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user wallet address should consist of 8 digits")
    public void userWalletAddressIsValidTest() {
        User user = generateUserWithWalletAddress("12345678");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user wallet address of size other than 8 should raise an error.")
    public void userWalletAddressIsInvalidTest() {
        User user = generateUserWithWalletAddress("123");
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

    private User generateUserWithFirstname(String firstname) {
        User userWithFirstname = generateValidUser();
        userWithFirstname.setFirstname(firstname);
        return userWithFirstname;
    }

    private User generateUserWithPassword(String password) {
        User userWithPassword = generateValidUser();
        userWithPassword.setPassword(password);
        return userWithPassword;
    }

    private User generateUserWithEmail(String email) {
        User userWithEmail = generateValidUser();
        userWithEmail.setEmail(email);
        return userWithEmail;
    }

    private User generateUserWhitAddress(String address) {
        User userWithAddress = generateValidUser();
        userWithAddress.setAddress(address);
        return userWithAddress;
    }

    private User generateUserWhitCVU(String cvu) {
        User userWithCVU = generateValidUser();
        userWithCVU.setCvuMercadoPago(cvu);
        return userWithCVU;
    }

    private User generateUserWithWalletAddress(String walletAddress) {
        User userWhitWalletAddress = generateValidUser();
        userWhitWalletAddress.setWalletAddress(walletAddress);
        return userWhitWalletAddress;
    }

}
