package org.example.orderservice.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOrderSaga {
    private final StateMachineService<States, Events> stateMachineService;

    public void orderComplete(Integer orderId) {
        StateMachine<States, Events> stateMachine = stateMachineService.acquireStateMachine(orderId.toString(), true);

        // Заказ оформлен
        Message<Events> msg = MessageBuilder.withPayload(Events.ORDER_COMPLETE)
                .setHeader("orderId", orderId).build();

        stateMachine.sendEvent(msg);
    }

    public void orderFailed(Integer orderId) {
        StateMachine<States, Events> stateMachine = stateMachineService.acquireStateMachine(orderId.toString(), true);

        // Заказ завершился неудачей
        Message<Events> msg = MessageBuilder.withPayload(Events.ORDER_FAILED)
                .setHeader("orderId", orderId).build();

        stateMachine.sendEvent(msg);
    }

    public void stockUpdate(Integer orderId) {
        StateMachine<States, Events> stateMachine = stateMachineService.acquireStateMachine(orderId.toString(), true);

        // Обновление склада
        Message<Events> msg = MessageBuilder.withPayload(Events.STOCK_UPDATE)
                .setHeader("orderId", orderId).build();

        stateMachine.sendEvent(msg);
    }

    public void stockRollback(Integer orderId) {
        StateMachine<States, Events> stateMachine = stateMachineService.acquireStateMachine(orderId.toString(), true);

        // Обновление склада
        Message<Events> msg = MessageBuilder.withPayload(Events.STOCK_ROLLBACK)
                .setHeader("orderId", orderId).build();

        stateMachine.sendEvent(msg);
    }

    public void createPayment(Integer orderId) {
        StateMachine<States, Events> stateMachine = stateMachineService.acquireStateMachine(orderId.toString(), true);

        // Создание платежа
        Message<Events> msg = MessageBuilder.withPayload(Events.PAYMENT_CREATE)
                .setHeader("orderId", orderId).build();

        stateMachine.sendEvent(msg);
    }

    public void cancelPayment(Integer orderId) {
        StateMachine<States, Events> stateMachine = stateMachineService.acquireStateMachine(orderId.toString(), true);

        // Отмена платежа
        Message<Events> msg = MessageBuilder.withPayload(Events.PAYMENT_CANCEL)
                .setHeader("orderId", orderId).build();

        stateMachine.sendEvent(msg);
    }

    public void createDelivery(Integer orderId) {
        StateMachine<States, Events> stateMachine = stateMachineService.acquireStateMachine(orderId.toString(), true);

        // Создание доставки
        Message<Events> msg = MessageBuilder.withPayload(Events.DELIVERY_CREATE)
                .setHeader("orderId", orderId).build();

        stateMachine.sendEvent(msg);
    }
}
