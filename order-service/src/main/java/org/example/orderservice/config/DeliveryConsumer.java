package org.example.orderservice.config;

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.event.DeliveryCreateEvent;
import org.example.orderservice.event.Result;
import org.example.orderservice.saga.CreateOrderSaga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DeliveryConsumer {
    private final CreateOrderSaga createOrderSaga;

    @Bean
    public Consumer<DeliveryCreateEvent> deliveryCreate() {
        return (delivery -> {
            if (Result.SUCCESS.equals(delivery.getResult())) {
                createOrderSaga.orderComplete(delivery.getOrderId());
            } else {
                createOrderSaga.cancelPayment(delivery.getOrderId());
            }
        });
    }
}
