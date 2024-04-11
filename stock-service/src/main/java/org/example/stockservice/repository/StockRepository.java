package org.example.stockservice.repository;

import java.util.Optional;

import org.example.stockservice.entity.Stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Optional<Stock> findByProductId(Integer productId);
}
