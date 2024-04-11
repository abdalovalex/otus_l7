package org.example.orderservice.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StockUpdateEvent {
    private Integer orderId;
    private Integer productId;
    private Integer count;
    private Double price;
    private Result result;
}