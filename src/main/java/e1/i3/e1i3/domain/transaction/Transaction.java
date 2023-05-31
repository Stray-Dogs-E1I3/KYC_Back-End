package e1.i3.e1i3.domain.transaction;

import e1.i3.e1i3.domain.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Transaction extends BaseTime {
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
    private String activity;

    @Column
    private Long timeStamp;

    @Column
    private BigDecimal gasUsed;

}
