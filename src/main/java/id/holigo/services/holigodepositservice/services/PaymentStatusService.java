package id.holigo.services.holigodepositservice.services;

import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigodepositservice.events.PaymentStatusEvent;
import org.springframework.statemachine.StateMachine;

public interface PaymentStatusService {

    StateMachine<PaymentStatusEnum, PaymentStatusEvent> paymentHasBeenSet(Long depositTransactionId);

    StateMachine<PaymentStatusEnum, PaymentStatusEvent> depositTransactionHasBeenPaid(Long depositTransactionId);

    StateMachine<PaymentStatusEnum, PaymentStatusEvent> depositTransactionHasBeenExpired(Long depositTransactionId);

    StateMachine<PaymentStatusEnum, PaymentStatusEvent> depositTransactionHasBeenCanceled(Long depositTransactionId);
}
