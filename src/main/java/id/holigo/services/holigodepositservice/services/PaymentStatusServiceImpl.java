package id.holigo.services.holigodepositservice.services;

import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigodepositservice.domain.DepositTransaction;
import id.holigo.services.holigodepositservice.events.PaymentStatusEvent;
import id.holigo.services.holigodepositservice.interceptors.PaymentStatusInterceptor;
import id.holigo.services.holigodepositservice.repositories.DepositTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentStatusServiceImpl implements PaymentStatusService {


    private DepositTransactionRepository depositTransactionRepository;

    private final StateMachineFactory<PaymentStatusEnum, PaymentStatusEvent> stateMachineFactory;

    private final PaymentStatusInterceptor paymentStatusInterceptor;


    public static final String DEPOSIT_TRANSACTION_HEADER = "deposit_transaction_id";

    @Autowired
    public void setDepositTransactionRepository(DepositTransactionRepository depositTransactionRepository) {
        this.depositTransactionRepository = depositTransactionRepository;
    }

    @Override
    public StateMachine<PaymentStatusEnum, PaymentStatusEvent> paymentHasBeenSet(Long depositTransactionId) {
        StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = build(depositTransactionId);
        sendEvent(depositTransactionId, sm, PaymentStatusEvent.PAYMENT_HAS_SET);
        return sm;
    }

    @Override
    public StateMachine<PaymentStatusEnum, PaymentStatusEvent> depositTransactionHasBeenPaid(Long depositTransactionId) {
        StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = build(depositTransactionId);
        sendEvent(depositTransactionId, sm, PaymentStatusEvent.PAYMENT_PAID);
        return sm;
    }

    @Override
    public StateMachine<PaymentStatusEnum, PaymentStatusEvent> depositTransactionHasBeenExpired(Long depositTransactionId) {
        StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = build(depositTransactionId);
        sendEvent(depositTransactionId, sm, PaymentStatusEvent.PAYMENT_EXPIRED);
        return sm;
    }

    @Override
    public StateMachine<PaymentStatusEnum, PaymentStatusEvent> depositTransactionHasBeenCanceled(Long depositTransactionId) {
        StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = build(depositTransactionId);
        sendEvent(depositTransactionId, sm, PaymentStatusEvent.PAYMENT_CANCEL);
        return sm;
    }

    private void sendEvent(Long id, StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm,
                           PaymentStatusEvent event) {
        Message<PaymentStatusEvent> message = MessageBuilder.withPayload(event)
                .setHeader(DEPOSIT_TRANSACTION_HEADER, id).build();
        sm.sendEvent(message);
    }

    private StateMachine<PaymentStatusEnum, PaymentStatusEvent> build(Long id) {
        DepositTransaction depositTransaction = depositTransactionRepository.getById(id);

        StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = stateMachineFactory
                .getStateMachine(depositTransaction.getId().toString());

        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(paymentStatusInterceptor);
            sma.resetStateMachine(new DefaultStateMachineContext<>(
                    depositTransaction.getPaymentStatus(), null, null, null));
        });
        sm.start();
        return sm;
    }
}
