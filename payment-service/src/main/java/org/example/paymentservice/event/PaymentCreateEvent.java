package org.example.paymentservice.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentCreateEvent implements SagaEvent {
    private Integer orderId;
    private Integer userId;
    private Double price;
    private Result result;
}