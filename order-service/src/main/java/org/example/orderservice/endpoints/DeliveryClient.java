package org.example.orderservice.endpoints;

import org.example.orderservice.dto.DeliveryDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "delivery", url = "${app.delivery-service}")
public interface DeliveryClient {
    @RequestMapping(method = RequestMethod.GET, value = "/delivery-service/delivery/{orderId}", produces = "application/json")
    DeliveryDTO getDelivery(@PathVariable("orderId") Integer orderId);
}
