package id.holigo.services.holigodepositservice.config;

import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigodepositservice.events.PaymentStatusEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Slf4j
@EnableStateMachineFactory(name = "paymentStatusTransactionSMF")
@Configuration
public class PaymentTransactionSMConfig extends StateMachineConfigurerAdapter<PaymentStatusEnum, PaymentStatusEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<PaymentStatusEnum, PaymentStatusEvent> states) throws Exception {
        states.withStates().initial(PaymentStatusEnum.SELECTING_PAYMENT)
                .states(EnumSet.allOf(PaymentStatusEnum.class))
                .end(PaymentStatusEnum.PAYMENT_FAILED)
                .end(PaymentStatusEnum.PAYMENT_CANCELED)
                .end(PaymentStatusEnum.PAYMENT_EXPIRED)
                .end(PaymentStatusEnum.REFUNDED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentStatusEnum, PaymentStatusEvent> transitions)
            throws Exception {
        transitions.withExternal().source(PaymentStatusEnum.SELECTING_PAYMENT).target(PaymentStatusEnum.WAITING_PAYMENT)
                .event(PaymentStatusEvent.PAYMENT_HAS_SET)
                .and().withExternal().source(PaymentStatusEnum.WAITING_PAYMENT).target(PaymentStatusEnum.PAID)
                .event(PaymentStatusEvent.PAYMENT_PAID)
                .and().withExternal().source(PaymentStatusEnum.SELECTING_PAYMENT).target(PaymentStatusEnum.PAYMENT_EXPIRED)
                .event(PaymentStatusEvent.PAYMENT_EXPIRED)
                .and().withExternal().source(PaymentStatusEnum.WAITING_PAYMENT).target(PaymentStatusEnum.PAYMENT_EXPIRED)
                .event(PaymentStatusEvent.PAYMENT_EXPIRED)
                .and().withExternal().source(PaymentStatusEnum.SELECTING_PAYMENT).target(PaymentStatusEnum.PAYMENT_CANCELED)
                .event(PaymentStatusEvent.PAYMENT_CANCEL)
                .and().withExternal().source(PaymentStatusEnum.WAITING_PAYMENT).target(PaymentStatusEnum.PAYMENT_CANCELED)
                .event(PaymentStatusEvent.PAYMENT_CANCEL);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentStatusEnum, PaymentStatusEvent> config)
            throws Exception {
        StateMachineListenerAdapter<PaymentStatusEnum, PaymentStatusEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<PaymentStatusEnum, PaymentStatusEvent> from,
                                     State<PaymentStatusEnum, PaymentStatusEvent> to) {
                log.info(String.format("stateChange(from: %s, to %s)", from.getId(), to.getId()));
            }
        };
        config.withConfiguration().listener(adapter);
    }
}
