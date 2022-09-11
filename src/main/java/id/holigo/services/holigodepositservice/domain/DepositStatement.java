package id.holigo.services.holigodepositservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DepositStatement {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private Long userId;

    private BigDecimal deposit;

    private BigDecimal debit;

    private BigDecimal credit;

    private String informationIndex;

    private String informationValue;

    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID transactionId;

    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID paymentId;

    private String transactionType;

    private String invoiceNumber;

    @Column(length = 20, columnDefinition = "varchar(20) default 'PAYMENT'")
    private String category = "PAYMENT";

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
