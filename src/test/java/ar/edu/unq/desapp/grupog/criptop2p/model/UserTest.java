package ar.edu.unq.desapp.grupog.criptop2p.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserTest {

    User user1;

    @BeforeEach

    public void setUp() {
        user1 = new User(1, "Jose", "Perez", "jperez@gmail.com", "Password.", "bernal", "4608738591410700747451", "45821674", null, null, null);
    }

    @Test
    void test01ANewUserIsCreatedAndIVerifyThatItsNameAndLastNameIsCorrect() {

        assertEquals(user1.getFirstname(), "Jose");
        assertEquals(user1.getLastname(), "Perez");
    }

    @Test
    void test02VerifyThatTheEmailAndPasswordAreCorrect() {

        assertEquals(user1.getEmail(), "jperez@gmail.com");
        assertEquals(user1.getPassword(), "Password.");
    }

    @Test
    void test03VerifyThatTheUserAddressIsCorrect() {

        assertEquals(user1.getAddress(), "bernal");
    }

    @Test
    void test04VerifyThatTheCvuOfMarketPaymentIsCorrect() {
        assertEquals(user1.getCvuMercadoPago(),"4608738591410700747451");
    }

    @Test
    void test05VerifyThatTheAddressOfTheCryptoAssetWalletIsCorrect() {

        assertEquals(user1.getWalletAddress(),"45821674" );
    }
}