package org.example.stockservice.config;

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stockservice.event.StockRollbackEvent;
import org.example.stockservice.event.StockUpdateEvent;
import org.example.stockservice.service.StockService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StockConsumer {
    private final StockService stockService;

    @Bean
    public Consumer<StockUpdateEvent> stockUpdate() {
        return (stockService::update);
    }

    @Bean
    public Consumer<StockRollbackEvent> stockRollback() {
        return (stockService::rollback);
    }
}
