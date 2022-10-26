package ar.edu.unq.desapp.grupog.criptop2p.persistence;

import ar.edu.unq.desapp.grupog.criptop2p.model.TransactionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionOrderRepository extends JpaRepository<TransactionOrder, Long> {
    @Query("SELECT t FROM TransactionOrder t " +
            "WHERE t.transactionStatus = 'CLOSED' " +
            "AND (t.dealerUser.id = :userId OR t.interestedUser.id = :userId)" +
            "AND t.creationDate BETWEEN :startDate AND :endDate")
    List<TransactionOrder> getUserClosedTransactionsBetweenDates(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
