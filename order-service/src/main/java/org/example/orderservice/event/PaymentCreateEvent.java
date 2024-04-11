package org.example.orderservice.event;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentCreateEvent {
    private Integer orderId;
    private Integer userId;
    private Double price;
    private Result result;
}
