package org.example.orderservice.config;

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.event.Result;
import org.example.orderservice.event.StockRollbackEvent;
import org.example.orderservice.event.StockUpdateEvent;
import org.example.orderservice.saga.CreateOrderSaga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StockConsumer {
    private final CreateOrderSaga createOrderSaga;

    @Bean
    public Consumer<StockUpdateEvent> stockUpdate() {
        return (stock -> {
            if (Result.SUCCESS.equals(stock.getResult())) {
                createOrderSaga.createPayment(stock.getOrderId());
            } else {
                createOrderSaga.orderFailed(stock.getOrderId());
            }
        });
    }

    @Bean
    public Consumer<StockRollbackEvent> stockRollback() {
        return (stock -> {
            if (Result.SUCCESS.equals(stock.getResult())) {
                createOrderSaga.orderFailed(stock.getOrderId());
            }
        });
    }
}
