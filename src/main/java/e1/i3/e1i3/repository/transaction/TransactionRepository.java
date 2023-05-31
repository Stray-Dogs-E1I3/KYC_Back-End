package e1.i3.e1i3.repository.transaction;

import e1.i3.e1i3.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long>,TransactionRepositoryCustom{
}
