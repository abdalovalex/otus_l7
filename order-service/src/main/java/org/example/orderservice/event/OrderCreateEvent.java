package org.example.orderservice.event;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderCreateEvent {
    private final Integer orderId;
}
