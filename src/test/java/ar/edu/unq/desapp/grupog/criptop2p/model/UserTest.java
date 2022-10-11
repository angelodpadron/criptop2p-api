package ar.edu.unq.desapp.grupog.criptop2p.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserTest {

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

    @Test
    @DisplayName("Initially a user have no roles")
    void aUserWithoutRolesTest() {
        User user = new User();
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    @DisplayName("A role can be assigned to a user")
    void aUserWithRolesTest() {
        User user = new User();
        Role role = mock(Role.class);
        when(role.getName()).thenReturn("USER_ROLE");

        user.addRole(role);

        assertTrue(user.getRoles().contains(role));
        assertEquals("USER_ROLE", user.getRoles().get(0).getName());
    }

    @Test
    @DisplayName("Initially a user have no market orders")
    void aUserWithoutMarketOrdersTest() {
        User user = new User();
        assertTrue(user.getMarketOrders().isEmpty());
    }

    @Test
    @DisplayName("A market order can be assigned to a user")
    void aUserWithMarketOrdersTest() {
        User user = new User();
        MarketOrder marketOrder = mock(MarketOrder.class);

        user.addMarketOrder(marketOrder);

        assertTrue(user.getMarketOrders().contains(marketOrder));
    }

    @Test
    @DisplayName("Initially a user have no transaction orders")
    void aUserWithoutTransactionOrdersTest() {
        User user = new User();
        assertTrue(user.getTransactionOrders().isEmpty());
    }

    @Test
    @DisplayName("A transaction order can be assigned to a user")
    void aUserWithTransactionOrdersTest() {
        User user = new User();
        TransactionOrder transactionOrder = mock(TransactionOrder.class);

        user.addTransactionOrder(transactionOrder);

        assertTrue(user.getTransactionOrders().contains(transactionOrder));
    }

}