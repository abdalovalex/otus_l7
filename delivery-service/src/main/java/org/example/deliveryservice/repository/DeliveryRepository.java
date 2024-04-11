package org.example.deliveryservice.repository;

import java.util.Optional;

import org.example.deliveryservice.entity.Delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    Optional<Delivery> findByOrderId(Integer orderId);
    Optional<Delivery> findFirstByOrderByDeliveryDateDesc();
}
