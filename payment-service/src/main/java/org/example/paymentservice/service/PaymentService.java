package org.example.paymentservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.entity.Payment;
import org.example.paymentservice.event.PaymentCancelEvent;
import org.example.paymentservice.event.PaymentCreateEvent;
import org.example.paymentservice.event.Result;
import org.example.paymentservice.event.SagaEvent;
import org.example.paymentservice.repository.PaymentRepository;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final StreamBridge streamBridge;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void create(PaymentCreateEvent paymentCreateEvent) {
        Payment payment = paymentRepository.findByOrderId(paymentCreateEvent.getOrderId()).orElse(null);
        if (payment != null) {
            paymentCreateEvent.setResult(Result.FAILED);
        } else {
            Payment newPayment = new Payment();
            newPayment.setOrderId(paymentCreateEvent.getOrderId());
            newPayment.setUserid(paymentCreateEvent.getUserId());
            newPayment.setPrice(paymentCreateEvent.getPrice());
            newPayment.setResult(Result.SUCCESS);

            paymentRepository.save(newPayment);
            paymentCreateEvent.setResult(Result.SUCCESS);
        }
        sendMessage(paymentCreateEvent, "paymentCreate");
    }

    @Transactional
    public void cancel(PaymentCancelEvent paymentCancelEvent) {
        Payment payment = paymentRepository.findByOrderId(paymentCancelEvent.getOrderId()).orElse(null);
        if (payment == null) {
            paymentCancelEvent.setResult(Result.FAILED);
        } else {
            payment.setResult(Result.CANCEL);
            paymentRepository.save(payment);
        }
        sendMessage(paymentCancelEvent, "paymentCancel");
    }

    private void sendMessage(SagaEvent event, String type) {
        streamBridge.send("orderSagaReplies",
                          MessageBuilder.withPayload(event)
                                  .setHeader("type", type).build());
    }
}
