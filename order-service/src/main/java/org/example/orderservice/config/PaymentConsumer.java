package org.example.orderservice.config;

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.event.PaymentCancelEvent;
import org.example.orderservice.event.PaymentCreateEvent;
import org.example.orderservice.event.Result;
import org.example.orderservice.saga.CreateOrderSaga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class PaymentConsumer {
    private final CreateOrderSaga createOrderSaga;

    @Bean
    public Consumer<PaymentCreateEvent> paymentCreate() {
        return (payment -> {
            if (Result.SUCCESS.equals(payment.getResult())) {
                createOrderSaga.createDelivery(payment.getOrderId());
            } else {
                createOrderSaga.stockRollback(payment.getOrderId());
            }
        });
    }

    @Bean
    public Consumer<PaymentCancelEvent> paymentCancel() {
        return (payment -> {
            if (Result.SUCCESS.equals(payment.getResult())) {
                createOrderSaga.stockRollback(payment.getOrderId());
            }
        });
    }
}
