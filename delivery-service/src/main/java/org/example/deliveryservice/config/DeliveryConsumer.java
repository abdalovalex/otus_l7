package org.example.deliveryservice.config;

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.deliveryservice.event.DeliveryCreateEvent;
import org.example.deliveryservice.service.DeliveryService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DeliveryConsumer {
    private final DeliveryService deliveryService;

    @Bean
    public Consumer<DeliveryCreateEvent> deliveryCreate() {
        return (deliveryService::create);
    }
}
