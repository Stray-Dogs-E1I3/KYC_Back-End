package e1.i3.e1i3.repository.transaction;

import e1.i3.e1i3.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long>,TransactionRepositoryCustom{
    Optional<Transaction> findByTransactionHash(String transactionHash);

    List<Transaction> findByUserAddressAndTimeStampBetween(String userAddress, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Transaction> findByUserAddress(String userAddress);

    @Query(value ="SELECT COUNT(t) FROM Transaction t WHERE t.userAddress = :userAddress AND FUNCTION('DATE_FORMAT', t.timeStamp, '%Y-%m') = FUNCTION('DATE_FORMAT', :date, '%Y-%m') AND t.method = :method")
    Long getCountTransactionsMethod(@Param("userAddress")String userAddress, @Param("method") String method, @Param("date")LocalDate date);
    @Query(value ="SELECT COUNT(t) FROM Transaction t WHERE t.userAddress = :userAddress AND FUNCTION('DATE_FORMAT', t.timeStamp, '%Y-%m') = FUNCTION('DATE_FORMAT', :date, '%Y-%m')")
    Long getTotTransactionsMethod(@Param("userAddress")String userAddress, @Param("date") LocalDate date);
}
