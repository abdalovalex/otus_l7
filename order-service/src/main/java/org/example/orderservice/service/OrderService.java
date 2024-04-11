package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.dto.DeliveryDTO;
import org.example.orderservice.dto.OrderDTO;
import org.example.orderservice.endpoints.DeliveryClient;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderStatus;
import org.example.orderservice.exception.OrderCantCreateException;
import org.example.orderservice.exception.OrderNotFoundException;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.saga.CreateOrderSaga;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final CreateOrderSaga createOrderSaga;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final TransactionTemplate transactionTemplate;
    private final DeliveryClient deliveryClient;

    public Integer create(OrderDTO orderDTO) throws OrderCantCreateException {
        final Order order = orderMapper.map(orderDTO);
        Integer orderId = transactionTemplate.execute(status -> {
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);
            return order.getId();
        });

        if (orderId == null) {
            throw new OrderCantCreateException("Не удалось создать заказ");
        }
        createOrderSaga.stockUpdate(orderId);

        return orderId;
    }

    public OrderDTO getOrder(Integer orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException("Не удалось найти заказ");
        }

        OrderDTO orderDTO = orderMapper.map(order);

        DeliveryDTO delivery = null;
        try {
            delivery = deliveryClient.getDelivery(orderId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (delivery != null) {
            orderDTO.setDeliveryDate(delivery.getDeliveryDate());
        }

        return orderDTO;
    }
}
