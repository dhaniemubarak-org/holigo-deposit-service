package id.holigo.services.holigodepositservice.services;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.holigodepositservice.domain.DepositTransaction;
import id.holigo.services.holigodepositservice.events.OrderStatusEvent;
import id.holigo.services.holigodepositservice.interceptors.OrderStatusInterceptor;
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
public class OrderStatusServiceImpl implements OrderStatusService {

    public static final String DEPOSIT_TRANSACTION_HEADER = "deposit_transaction_id";

    private final StateMachineFactory<OrderStatusEnum, OrderStatusEvent> orderStatusStateMachineFactory;

    private final OrderStatusInterceptor orderStatusTransactionInterceptor;

    private DepositTransactionRepository depositTransactionRepository;

    @Autowired
    public void setDepositTransactionRepository(DepositTransactionRepository depositTransactionRepository) {
        this.depositTransactionRepository = depositTransactionRepository;
    }

    @Override
    public StateMachine<OrderStatusEnum, OrderStatusEvent> issuedSuccess(Long depositTransactionId) {
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = build(depositTransactionId);
        sendEvent(depositTransactionId, sm, OrderStatusEvent.ISSUED_SUCCESS);
        return sm;
    }

    @Override
    public StateMachine<OrderStatusEnum, OrderStatusEvent> issuedFail(Long depositTransactionId) {
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = build(depositTransactionId);
        sendEvent(depositTransactionId, sm, OrderStatusEvent.ISSUED_FAIL);
        return sm;
    }

    @Override
    public StateMachine<OrderStatusEnum, OrderStatusEvent> cancelTransaction(Long depositTransactionId) {
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = build(depositTransactionId);
        sendEvent(depositTransactionId, sm, OrderStatusEvent.ORDER_CANCEL);
        return sm;
    }

    @Override
    public StateMachine<OrderStatusEnum, OrderStatusEvent> expiredTransaction(Long depositTransactionId) {
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = build(depositTransactionId);
        sendEvent(depositTransactionId, sm, OrderStatusEvent.ORDER_EXPIRE);
        return sm;
    }

    private void sendEvent(Long id, StateMachine<OrderStatusEnum, OrderStatusEvent> sm,
                           OrderStatusEvent event) {
        Message<OrderStatusEvent> message = MessageBuilder.withPayload(event)
                .setHeader(DEPOSIT_TRANSACTION_HEADER, id).build();
        sm.sendEvent(message);
    }

    private StateMachine<OrderStatusEnum, OrderStatusEvent> build(Long id) {
        DepositTransaction depositTransaction = depositTransactionRepository.getById(id);

        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = orderStatusStateMachineFactory
                .getStateMachine(depositTransaction.getId().toString());

        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(orderStatusTransactionInterceptor);
            sma.resetStateMachine(new DefaultStateMachineContext<>(
                    depositTransaction.getOrderStatus(), null, null, null));
        });
        sm.start();
        return sm;
    }
}
