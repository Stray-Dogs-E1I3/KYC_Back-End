package e1.i3.e1i3.service.transaction;

import e1.i3.e1i3.dto.tnxs.CalendarResDTO;
import e1.i3.e1i3.dto.tnxs.DailyTnxs;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    public void saveRecentTransactionList(String userAddress) throws IOException;

    public List<DailyTnxs> getDailyTransactionList(String userAddress, LocalDate date);
    public List<CalendarResDTO> getDailyGasfeeInCalendarView(String userAddress);
}
