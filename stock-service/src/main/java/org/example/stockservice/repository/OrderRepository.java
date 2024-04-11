package org.example.stockservice.repository;

import java.util.Optional;

import org.example.stockservice.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByOrderId(Integer orderId);
}
