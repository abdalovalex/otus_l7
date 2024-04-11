package org.example.orderservice.saga.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderStatus;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.saga.Events;
import org.example.orderservice.saga.States;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderFailedAction implements Action<States, Events> {
    private final OrderRepository orderRepository;

    @Override
    public void execute(StateContext<States, Events> stateContext) {
        Integer orderId = (Integer) stateContext.getMessage().getHeaders().get("orderId");

        if (orderId == null) {
            throw new RuntimeException("Не найден заказ");
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("Не найден заказ");
        } else {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
        }
    }
}
