package org.example.paymentservice.config;

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.event.PaymentCancelEvent;
import org.example.paymentservice.event.PaymentCreateEvent;
import org.example.paymentservice.service.PaymentService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class PaymentConsumer {
    private final PaymentService paymentService;

    @Bean
    public Consumer<PaymentCreateEvent> paymentCreate() {
        return (paymentService::create);
    }

    @Bean
    public Consumer<PaymentCancelEvent> paymentCancel() {
        return (paymentService::cancel);
    }
}
