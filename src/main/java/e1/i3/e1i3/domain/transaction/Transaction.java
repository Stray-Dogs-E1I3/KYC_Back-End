package e1.i3.e1i3.domain.transaction;

import e1.i3.e1i3.domain.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    private Long id;

    @Column
    private String userAddress;

    @Column
    private String transactionHash;

    @Column
    private String method;

    @Column
    private String protocol;

    @Column
    private LocalDateTime timeStamp;

    @Column
    private double gasUsed;
}
