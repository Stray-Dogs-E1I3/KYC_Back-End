package e1.i3.e1i3.repository.transaction;

import e1.i3.e1i3.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long>,TransactionRepositoryCustom{
    Optional<Transaction> findByTransactionHash(String transactionHash);

    List<Transaction> findByUserAddressAndTimeStampBetween(String userAddress, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Transaction> findByUserAddress(String userAddress);
}
