package id.holigo.services.common.model;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositTransactionDto implements Serializable {

    private Long id;

    private Long userId;

    private String phoneNumber;

    private BigDecimal nominal;

    private BigDecimal fareAmount;

    private BigDecimal ntaAmount;

    private BigDecimal nraAmount;

    private BigInteger discountAmount;

    private String paymentServiceId;

    private String device;

    private Integer productId;

    private PaymentStatusEnum paymentStatus;

    private OrderStatusEnum orderStatus;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private BigDecimal cpAmount;

    private BigDecimal mpAmount;

    private BigDecimal ipAmount;

    private BigDecimal hpAmount;

    private BigDecimal hvAmount;

    private BigDecimal prAmount;

    private BigDecimal ipcAmount;

    private BigDecimal hpcAmount;

    private BigDecimal prcAmount;

    private BigDecimal lossAmount;

    private UUID transactionId;

    private UUID paymentId;

    private String invoiceNumber;
}
