package org.example.orderservice.event;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentCancelEvent {
    private Integer orderId;
    private Result result;
}
