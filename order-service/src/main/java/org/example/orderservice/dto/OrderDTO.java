package org.example.orderservice.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.example.orderservice.entity.OrderStatus;
import org.example.orderservice.event.Result;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {
    private Integer orderId;
    private Integer userId;
    private Integer count;
    private Integer productId;
    private Double price;
    private OrderStatus status;
    private LocalDate deliveryDate;
}
