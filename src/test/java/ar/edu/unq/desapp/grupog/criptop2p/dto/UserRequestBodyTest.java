package ar.edu.unq.desapp.grupog.criptop2p.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRequestBodyTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("The username can have three to thirty characters")
    public void usernameLengthCanHaveThreeToThirtyCharactersTest() {
        UserRequestBody user = generateUserRequestBodyWithFirstname("Joseph Joestar");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("The username cannot cannot have less than three characters")
    public void usernameCannotHaveLessThanThreeCharactersTest() {
        UserRequestBody user = generateUserRequestBodyWithFirstname("Ty");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("The username cannot have more than thirty characters")
    public void usernameCannotHaveMoreThanThirtyCharactersTest() {
        UserRequestBody user = generateUserRequestBodyWithFirstname("Wolfeschlegelsteinhausenbergerdorff");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A valid user email should not raise a validation error")
    public void userEmailIsValidTest() {
        UserRequestBody user = generateUserRequestBodyWithEmail("jdoe@gmail.com");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("An invalid user email should raise a validation error")
    public void userEmailIsInvalidTest() {
        UserRequestBody user = generateUserRequestBodyWithEmail("Obviously not an email");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user password should contain an uppercase, a lowercase, a number and a special character")
    public void userPasswordIsValidPasswordTest() {
        UserRequestBody user = generateUserRequestBodyWithPassword("passworD@1");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user password without an uppercase, a lowercase, a number and a special character should raise a validation error")
    public void userPasswordIsInvalidTest() {
        UserRequestBody user = generateUserRequestBodyWithEmail("123");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user address can have three to thirty characters")
    public void userAddressIsValidTest() {
        UserRequestBody user = generateUserRequestBodyWhitAddress("Florida Avenue");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user address cannot have less than three characters")
    public void userAddressCannotHaveLessThanThreeCharactersTest() {
        UserRequestBody user = generateUserRequestBodyWhitAddress("St");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user address cannot have more than thirty characters")
    public void userAddressCannotHaveMoreThanThirtyCharactersTest() {
        UserRequestBody user = generateUserRequestBodyWhitAddress("Wolfeschlegelsteinhausenbergerdorff Street");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user cvu should consist of 22 digits")
    public void userCVUIsValidTest() {
        UserRequestBody user = generateUserRequestBodyWhitCVU("4608738591410700747451");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user CVU of size other than 22 should raise an error.")
    public void userCVUIsInvalidTest() {
        UserRequestBody user = generateUserRequestBodyWhitCVU("123");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("A user wallet address should consist of 8 digits")
    public void userWalletAddressIsValidTest() {
        UserRequestBody user = generateUserRequestBodyWithWalletAddress("12345678");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("A user wallet address of size other than 8 should raise an error.")
    public void userWalletAddressIsInvalidTest() {
        UserRequestBody user = generateUserRequestBodyWithWalletAddress("123");
        Set<ConstraintViolation<UserRequestBody>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
    }


    private UserRequestBody generateValidUserRequestBody() {
        UserRequestBody validUserRequestBody = new UserRequestBody();
        validUserRequestBody.setFirstname("John");
        validUserRequestBody.setLastname("Doe");
        validUserRequestBody.setEmail("jdoe@gmail.com");
        validUserRequestBody.setPassword("Password@1");
        validUserRequestBody.setAddress("Florida Avenue");
        validUserRequestBody.setCvuMercadoPago("4608738591410700747451");
        validUserRequestBody.setWalletAddress("45821674");

        return validUserRequestBody;
    }

    private UserRequestBody generateUserRequestBodyWithFirstname(String firstname) {
        UserRequestBody userWithFirstname = generateValidUserRequestBody();
        userWithFirstname.setFirstname(firstname);
        return userWithFirstname;
    }

    private UserRequestBody generateUserRequestBodyWithPassword(String password) {
        UserRequestBody userWithPassword = generateValidUserRequestBody();
        userWithPassword.setPassword(password);
        return userWithPassword;
    }

    private UserRequestBody generateUserRequestBodyWithEmail(String email) {
        UserRequestBody userWithEmail = generateValidUserRequestBody();
        userWithEmail.setEmail(email);
        return userWithEmail;
    }

    private UserRequestBody generateUserRequestBodyWhitAddress(String address) {
        UserRequestBody userWithAddress = generateValidUserRequestBody();
        userWithAddress.setAddress(address);
        return userWithAddress;
    }

    private UserRequestBody generateUserRequestBodyWhitCVU(String cvu) {
        UserRequestBody userWithCVU = generateValidUserRequestBody();
        userWithCVU.setCvuMercadoPago(cvu);
        return userWithCVU;
    }

    private UserRequestBody generateUserRequestBodyWithWalletAddress(String walletAddress) {
        UserRequestBody userWhitWalletAddress = generateValidUserRequestBody();
        userWhitWalletAddress.setWalletAddress(walletAddress);
        return userWhitWalletAddress;
    }

}
