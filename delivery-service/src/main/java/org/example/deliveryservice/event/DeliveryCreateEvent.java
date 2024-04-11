package org.example.deliveryservice.event;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryCreateEvent implements SagaEvent {
    private Integer orderId;
    private Integer userId;
    private LocalDate deliveryDate;
    private Result result;
}