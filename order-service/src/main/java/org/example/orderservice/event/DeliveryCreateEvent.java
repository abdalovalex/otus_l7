package org.example.orderservice.event;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeliveryCreateEvent {
    private final Integer orderId;
    private final Integer userId;
    private final LocalDateTime time;
    private final Result result;
}
