package e1.i3.e1i3.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class TimestampRange {
    public static LocalDateTime[] getCurrentTimestampRange() {
        LocalDate currentDate = LocalDate.of(2023, Month.MAY, 31);
        LocalDateTime start = LocalDateTime.of(currentDate, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(currentDate, LocalTime.MAX);

        LocalDateTime[] range = new LocalDateTime[2];
        range[0] = start;
        range[1] = end;

        return range;
    }

}
