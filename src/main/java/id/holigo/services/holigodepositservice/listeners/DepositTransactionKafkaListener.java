package id.holigo.services.holigodepositservice.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.DepositDto;
import id.holigo.services.common.model.DepositTransactionDto;
import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigodepositservice.config.KafkaTopicConfig;
import id.holigo.services.holigodepositservice.domain.DepositTransaction;
import id.holigo.services.holigodepositservice.repositories.DepositTransactionRepository;
import id.holigo.services.holigodepositservice.services.DepositService;
import id.holigo.services.holigodepositservice.services.OrderStatusService;
import id.holigo.services.holigodepositservice.services.PaymentStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Optional;

@Slf4j
@Component
public class DepositTransactionKafkaListener {

    private DepositTransactionRepository depositTransactionRepository;

    private PaymentStatusService paymentStatusService;

    private OrderStatusService orderStatusService;

    private DepositService depositService;

    @Autowired
    public void setDepositService(DepositService depositService) {
        this.depositService = depositService;
    }

    @Autowired
    public void setPaymentStatusService(PaymentStatusService paymentStatusService) {
        this.paymentStatusService = paymentStatusService;
    }

    @Autowired
    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @Autowired
    public void setDepositTransactionRepository(DepositTransactionRepository depositTransactionRepository) {
        this.depositTransactionRepository = depositTransactionRepository;
    }

    @KafkaListener(topics = KafkaTopicConfig.UPDATE_DEPOSIT_TRANSACTION, groupId = "update-deposit-transaction",
            containerFactory = "updateDepositTransactionContainerFactory")
    void updateDepositTransaction(DepositTransactionDto depositTransactionDto) {
        Optional<DepositTransaction> fetchDepositTransaction = depositTransactionRepository.findById(depositTransactionDto.getId());
        if (fetchDepositTransaction.isPresent()) {
            DepositTransaction depositTransaction = fetchDepositTransaction.get();
            if (depositTransactionDto.getPaymentStatus().equals(PaymentStatusEnum.PAYMENT_EXPIRED)) {
                paymentStatusService.depositTransactionHasBeenExpired(depositTransaction.getId());
                orderStatusService.expiredTransaction(depositTransaction.getId());
            }
            if (depositTransactionDto.getPaymentStatus().equals(PaymentStatusEnum.PAYMENT_CANCELED)) {
                paymentStatusService.depositTransactionHasBeenCanceled(depositTransaction.getId());
                orderStatusService.cancelTransaction(depositTransaction.getId());
            }
            depositTransaction.setPaymentStatus(depositTransactionDto.getPaymentStatus());
            if (depositTransactionDto.getPaymentStatus().equals(PaymentStatusEnum.WAITING_PAYMENT)) {
                paymentStatusService.paymentHasBeenSet(depositTransaction.getId());
                depositTransaction.setFareAmount(depositTransactionDto.getFareAmount());
                depositTransaction.setPaymentId(depositTransactionDto.getPaymentId());
                depositTransaction.setPaymentServiceId(depositTransactionDto.getPaymentServiceId());
                depositTransactionRepository.save(depositTransaction);
            }

            if (!depositTransaction.getPaymentStatus().equals(PaymentStatusEnum.PAID)
                    && depositTransactionDto.getPaymentStatus().equals(PaymentStatusEnum.PAID)) {
                paymentStatusService.depositTransactionHasBeenPaid(depositTransaction.getId());
                DepositDto depositDto = DepositDto.builder().transactionId(depositTransaction.getTransactionId())
                        .category("TOP_UP")
                        .creditAmount(depositTransaction.getFareAmount())
                        .isValid(false)
                        .invoiceNumber(depositTransaction.getInvoiceNumber())
                        .paymentId(depositTransaction.getPaymentId())
                        .userId(depositTransaction.getUserId())
                        .transactionId(depositTransaction.getTransactionId())
                        .transactionType("HTD")
                        .informationIndex("depositStatement.topUp").build();
                try {
                    depositService.credit(depositDto);
                } catch (JMSException | JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                if (depositDto.getIsValid()) {
                    orderStatusService.issuedSuccess(depositTransaction.getId());
                }
            }
        }
    }
}
