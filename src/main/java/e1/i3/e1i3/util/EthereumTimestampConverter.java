package e1.i3.e1i3.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class EthereumTimestampConverter {

    public static LocalDateTime convertToDateTime(long ethereumTimestamp) {
        return LocalDateTime.ofEpochSecond(ethereumTimestamp, 0, ZoneOffset.UTC);
    }
}
