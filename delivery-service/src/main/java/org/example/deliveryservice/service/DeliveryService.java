package org.example.deliveryservice.service;

import java.time.LocalDate;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.deliveryservice.dto.DeliveryDTO;
import org.example.deliveryservice.entity.Delivery;
import org.example.deliveryservice.event.DeliveryCreateEvent;
import org.example.deliveryservice.event.Result;
import org.example.deliveryservice.event.SagaEvent;
import org.example.deliveryservice.exception.OrderNotFoundException;
import org.example.deliveryservice.mapper.DeliveryMapper;
import org.example.deliveryservice.repository.DeliveryRepository;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
    private final StreamBridge streamBridge;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    @Transactional
    public void create(DeliveryCreateEvent deliveryCreateEvent) {
        Delivery delivery = deliveryRepository.findByOrderId(deliveryCreateEvent.getOrderId()).orElse(null);
        if (delivery != null) {
            deliveryCreateEvent.setResult(Result.FAILED);
        } else {
            Delivery deliveryWithMaxDate = deliveryRepository.findFirstByOrderByDeliveryDateDesc().orElse(null);
            LocalDate deliveryDate = LocalDate.now();
            if (deliveryWithMaxDate != null) {
                deliveryDate = deliveryWithMaxDate.getDeliveryDate().plusDays(1);
            }
            Delivery newDelivery = new Delivery();
            newDelivery.setOrderId(deliveryCreateEvent.getOrderId());
            newDelivery.setUserid(deliveryCreateEvent.getUserId());
            newDelivery.setDeliveryDate(deliveryDate);
            newDelivery.setResult(Result.SUCCESS);

            deliveryRepository.save(newDelivery);
            deliveryCreateEvent.setResult(Result.SUCCESS);
        }
        sendMessage(deliveryCreateEvent, "deliveryCreate");
    }

    public DeliveryDTO getDelivery(Integer orderId) throws OrderNotFoundException {
        Delivery delivery = deliveryRepository.findByOrderId(orderId).orElse(null);
        if (delivery == null) {
            throw new OrderNotFoundException("Не удалось найти заказ");
        }
        return deliveryMapper.map(delivery);
    }

    private void sendMessage(SagaEvent event, String type) {
        streamBridge.send("orderSagaReplies",
                          MessageBuilder.withPayload(event)
                                  .setHeader("type", type).build());
    }
}
