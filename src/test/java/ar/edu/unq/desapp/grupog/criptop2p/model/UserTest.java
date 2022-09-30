package ar.edu.unq.desapp.grupog.criptop2p.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserTest {

    private final User aValidUser = generateValidUser();

    @Test
    void test01ANewUserIsCreatedAndIVerifyThatItsNameAndLastNameIsCorrect() {
        assertEquals("Jose", aValidUser.getFirstname());
        assertEquals("Perez", aValidUser.getLastname());
    }

    @Test
    void test02VerifyThatTheEmailAndPasswordAreCorrect() {
        assertEquals("jperez@gmail.com", aValidUser.getEmail());
        assertEquals("Password@1", aValidUser.getPassword());
    }

    @Test
    void test03VerifyThatTheUserAddressIsCorrect() {
        assertEquals("Bernal", aValidUser.getAddress());
    }

    @Test
    void test04VerifyThatTheCvuOfMarketPaymentIsCorrect() {
        assertEquals("4608738591410700747451", aValidUser.getCvuMercadoPago());
    }

    @Test
    void test05VerifyThatTheAddressOfTheCryptoAssetWalletIsCorrect() {
        assertEquals("45821674", aValidUser.getWalletAddress());
    }

    @Test
    @DisplayName("A user initially has zero points")
    void aUserInitiallyHasZeroPointsTest() {
        User user = new User();
        assertEquals(0, user.getPoints());
    }

    @Test
    @DisplayName("The points of a user can be modified")
    void thePointsOfAUserCanBeModifiedTest() {
        User user = new User();
        user.setPoints(5);
        assertEquals(5, user.getPoints());
    }

    @Test
    @DisplayName("A user initially has zero operations")
    void aUserInitiallyHasZeroOperationsTest() {
        User user = new User();
        assertEquals(0, user.getOperations());
    }

    @Test
    @DisplayName("The number of operations of a user can be modified")
    void theNumberOfOperationsOfAUserCanBeModifiedTest() {
        User user = new User();
        user.setOperations(5);
        assertEquals(5, user.getOperations());
    }

    @Test
    @DisplayName("A user initially has a reputation of zero")
    void aUserInitiallyHasAReputationOfZeroTest() {
        User user = new User();
        assertEquals(0, user.getReputation());
    }

    @Test
    @DisplayName("A user with 10 points and 10 operations has a reputation of 1")
    void aUserWithTenPointsAndTenOperationsHasAReputationOfOneTest() {
        User user = new User();
        user.setPoints(10);
        user.setOperations(10);
        assertEquals(1, user.getReputation());
    }

    private User generateValidUser() {
        return new User("Jose",
                "Perez",
                "jperez@gmail.com",
                "Password@1",
                "Bernal",
                "4608738591410700747451",
                "45821674");
    }
}