package id.holigo.services.holigodepositservice.interceptors;

import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigodepositservice.domain.DepositTransaction;
import id.holigo.services.holigodepositservice.events.PaymentStatusEvent;
import id.holigo.services.holigodepositservice.repositories.DepositTransactionRepository;
import id.holigo.services.holigodepositservice.services.PaymentStatusServiceImpl;
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
public class PaymentStatusInterceptor extends StateMachineInterceptorAdapter<PaymentStatusEnum, PaymentStatusEvent> {

    private final DepositTransactionRepository depositTransactionRepository;

    @Override
    public void preStateChange(State<PaymentStatusEnum, PaymentStatusEvent> state, Message<PaymentStatusEvent> message,
                               Transition<PaymentStatusEnum, PaymentStatusEvent> transition,
                               StateMachine<PaymentStatusEnum, PaymentStatusEvent> stateMachine) {
        Optional.ofNullable(message).flatMap(msg -> Optional.of(Long.valueOf(Objects.requireNonNull(msg.getHeaders().get(PaymentStatusServiceImpl.DEPOSIT_TRANSACTION_HEADER)).toString()))).ifPresent(id -> {
            DepositTransaction depositTransaction = depositTransactionRepository.getById(id);
            depositTransaction.setPaymentStatus(state.getId());
            depositTransactionRepository.save(depositTransaction);
        });
    }
}
