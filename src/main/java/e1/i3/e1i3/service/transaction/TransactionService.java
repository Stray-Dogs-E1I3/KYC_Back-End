package e1.i3.e1i3.service.transaction;

import java.io.IOException;

public interface TransactionService {
    public void saveRecentTransactionList(String userAddress) throws IOException;
}
