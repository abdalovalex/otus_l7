package org.example.orderservice.saga.action;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.entity.Order;
import org.example.orderservice.event.StockUpdateEvent;
import org.example.orderservice.exception.OrderNotFoundException;
import org.example.orderservice.repository.OrderRepository;
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
@Transactional
public class StockUpdateAction implements Action<States, Events> {
    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;

    @SneakyThrows
    @Override
    public void execute(StateContext<States, Events> stateContext) {
        Integer orderId = (Integer) stateContext.getMessage().getHeaders().get("orderId");

        if (orderId == null) {
            throw new OrderNotFoundException("Не удалось найти заказ");
        }

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            throw new OrderNotFoundException("Не удалось найти заказ");
        }

        StockUpdateEvent stockUpdateEvent = StockUpdateEvent.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .count(order.getCount())
                .price(order.getPrice())
                .build();
        streamBridge.send("stockTopic",
                          MessageBuilder.withPayload(stockUpdateEvent)
                                  .build());
    }
}
