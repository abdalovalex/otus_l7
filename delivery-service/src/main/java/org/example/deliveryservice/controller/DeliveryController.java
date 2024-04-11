package org.example.deliveryservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.deliveryservice.dto.DeliveryDTO;
import org.example.deliveryservice.exception.OrderNotFoundException;
import org.example.deliveryservice.service.DeliveryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService service;

    @GetMapping("/{orderId}")
    public ResponseEntity<DeliveryDTO> get(@PathVariable Integer orderId) {
        try {
            return ResponseEntity.ok(service.getDelivery(orderId));
        } catch (OrderNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }
}
