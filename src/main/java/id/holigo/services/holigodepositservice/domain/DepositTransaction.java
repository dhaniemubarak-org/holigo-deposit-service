package id.holigo.services.holigodepositservice.domain;


import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.common.model.PaymentStatusEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class DepositTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private String phoneNumber;

    private String name;

    private BigDecimal nominal;

    private BigDecimal fareAmount;

    private BigDecimal ntaAmount;

    @Builder.Default
    private BigDecimal nraAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(length = 10)
    private String paymentServiceId;

    @Column(length = 10)
    private String device;

    private Integer productId;

    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum paymentStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;

    private Timestamp expiredAt;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @Builder.Default
    private BigDecimal cpAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal mpAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal ipAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal hpAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal hvAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal prAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal ipcAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal hpcAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal prcAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal lossAmount = BigDecimal.ZERO;

    @Column(length = 36, columnDefinition = "varchar(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID transactionId;

    @Column(length = 36, columnDefinition = "varchar(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID paymentId;

    private String invoiceNumber;

}
