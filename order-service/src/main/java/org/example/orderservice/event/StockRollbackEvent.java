package org.example.orderservice.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StockRollbackEvent {
    private Integer orderId;
    private Result result;
}