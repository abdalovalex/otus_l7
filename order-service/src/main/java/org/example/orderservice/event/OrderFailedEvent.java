package org.example.orderservice.event;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderFailedEvent {
    private final Integer orderId;
}
