package e1.i3.e1i3.service.transaction;

import e1.i3.e1i3.dto.tnxs.CalendarResDTO;
import e1.i3.e1i3.dto.tnxs.DailyTnxs;
import e1.i3.e1i3.dto.tnxs.Diagram;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    public void saveRecentTransactionList(String userAddress,String protocol,String network) throws IOException, InterruptedException;

    public void saveRecentTransaction(Object eventData) throws IOException;

    public List<DailyTnxs> getDailyTransactionList(String userAddress, LocalDate date);
    public List<CalendarResDTO> getDailyGasfeeInCalendarView(String userAddress, LocalDate date);
    public Diagram getDiagram(String userAddress, LocalDate date);
}
