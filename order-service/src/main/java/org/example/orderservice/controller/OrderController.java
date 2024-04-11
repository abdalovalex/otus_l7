package org.example.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.OrderDTO;
import org.example.orderservice.exception.OrderCantCreateException;
import org.example.orderservice.exception.OrderNotFoundException;
import org.example.orderservice.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody OrderDTO orderDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(orderDTO));
        } catch (OrderCantCreateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> get(@PathVariable Integer orderId) {
        try {
            return ResponseEntity.ok(service.getOrder(orderId));
        } catch (OrderNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
