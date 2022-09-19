package id.holigo.services.holigodepositservice.interceptors;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.holigodepositservice.domain.DepositTransaction;
import id.holigo.services.holigodepositservice.events.OrderStatusEvent;
import id.holigo.services.holigodepositservice.repositories.DepositTransactionRepository;
import id.holigo.services.holigodepositservice.services.OrderStatusServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OrderStatusInterceptor
        extends StateMachineInterceptorAdapter<OrderStatusEnum, OrderStatusEvent> {

    private final DepositTransactionRepository depositTransactionRepository;

    @Override
    public void preStateChange(State<OrderStatusEnum, OrderStatusEvent> state, Message<OrderStatusEvent> message,
                               Transition<OrderStatusEnum, OrderStatusEvent> transition,
                               StateMachine<OrderStatusEnum, OrderStatusEvent> stateMachine) {
        Optional.ofNullable(message).flatMap(msg -> Optional.of(Long.valueOf(Objects.requireNonNull(msg.getHeaders().get(OrderStatusServiceImpl.DEPOSIT_TRANSACTION_HEADER)).toString()))).ifPresent(id -> {
            DepositTransaction depositTransaction = depositTransactionRepository.getById(id);
            depositTransaction.setOrderStatus(state.getId());
            depositTransactionRepository.save(depositTransaction);
        });
    }

}
