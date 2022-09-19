package id.holigo.services.holigodepositservice.services;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.holigodepositservice.events.OrderStatusEvent;
import org.springframework.statemachine.StateMachine;

public interface OrderStatusService {

    StateMachine<OrderStatusEnum, OrderStatusEvent> issuedSuccess(Long depositTransactionId);

    StateMachine<OrderStatusEnum, OrderStatusEvent> issuedFail(Long depositTransactionId);

    StateMachine<OrderStatusEnum, OrderStatusEvent> cancelTransaction(Long depositTransactionId);

    StateMachine<OrderStatusEnum, OrderStatusEvent> expiredTransaction(Long depositTransactionIdÎ);
}
