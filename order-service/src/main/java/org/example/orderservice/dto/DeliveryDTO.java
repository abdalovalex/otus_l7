package org.example.orderservice.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryDTO {
    private Integer orderId;
    private Integer userId;
    private LocalDate deliveryDate;
}
