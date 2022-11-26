package ar.edu.unq.desapp.grupog.criptop2p.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
class UserTest {

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
    @DisplayName("The points of a user can be incremented")
    void incrementUserPointsTest() {
        User user = new User();
        user.addPoints(10);
        assertEquals(10, user.getPoints());
    }

    @Test
    @DisplayName("The points of a user are reduced by 20 as a penalty for cancellation of a transaction")
    void decrementUserPointsTest() {
        User user = new User();
        user.applyCancellationPenalty();
        assertEquals(-20, user.getPoints());
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

    @Test
    @DisplayName("A user can check if another user have the same email address")
    void twoUsersWithTheSameEmailTest() {
        User user1 = new User();
        User user2 = new User();
        String emailAddress = "something@email.com";
        user1.setEmail(emailAddress);
        user2.setEmail(emailAddress);

        assertTrue(user1.hasSameEmail(user1));
    }

    @Test
    @DisplayName("The points of a user can be modified")
    void thePointsOfAUserCanBeModifiedTest() {
        User user = new User();
        user.setPoints(5);
        assertEquals(5, user.getPoints());
    }

}