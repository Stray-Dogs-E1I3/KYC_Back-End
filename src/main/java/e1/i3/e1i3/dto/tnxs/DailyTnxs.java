package e1.i3.e1i3.dto.tnxs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
public class DailyTnxs {

    private String transactionHash;

    private String method;

    private String activity;

    private LocalDateTime timeStamp;

    private String gasUsed;
}
