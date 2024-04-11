package org.example.paymentservice.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentCancelEvent implements SagaEvent {
    private Integer orderId;
    private Result result;
}