package org.example.orderservice.saga.action;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.event.StockRollbackEvent;
import org.example.orderservice.exception.OrderNotFoundException;
import org.example.orderservice.saga.Events;
import org.example.orderservice.saga.States;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockRollbackAction implements Action<States, Events> {
    private final StreamBridge streamBridge;

    @SneakyThrows
    @Override
    public void execute(StateContext<States, Events> stateContext) {
        Integer orderId = (Integer) stateContext.getMessage().getHeaders().get("orderId");

        if (orderId == null) {
            throw new OrderNotFoundException("Не удалось найти заказ");
        }

        StockRollbackEvent stockRollbackEvent = StockRollbackEvent.builder()
                .orderId(orderId)
                .build();
        streamBridge.send("stockTopic",
                          MessageBuilder.withPayload(stockRollbackEvent)
                                  .build());
    }
}
