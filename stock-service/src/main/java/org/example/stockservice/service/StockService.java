package org.example.stockservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stockservice.entity.Order;
import org.example.stockservice.entity.Stock;
import org.example.stockservice.event.Result;
import org.example.stockservice.event.SagaEvent;
import org.example.stockservice.event.StockRollbackEvent;
import org.example.stockservice.event.StockUpdateEvent;
import org.example.stockservice.repository.OrderRepository;
import org.example.stockservice.repository.StockRepository;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    private final StreamBridge streamBridge;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void update(StockUpdateEvent stockUpdateEvent) {
        Stock stock = stockRepository.findByProductId(stockUpdateEvent.getProductId()).orElse(null);
        if (stock == null || stock.getCount() < stockUpdateEvent.getCount()) {
            stockUpdateEvent.setResult(Result.FAILED);
        } else {
            stock.setCount(stock.getCount() - stockUpdateEvent.getCount());
            stockUpdateEvent.setResult(Result.SUCCESS);
            stockRepository.save(stock);

            Order order = new Order();
            order.setOrderId(stockUpdateEvent.getOrderId());
            order.setCount(stockUpdateEvent.getCount());
            order.setPrice(stockUpdateEvent.getPrice());
            order.setResult(Result.SUCCESS);
            order.setStock(stock);

            orderRepository.save(order);
        }

        sendMessage(stockUpdateEvent, "stockUpdate");
    }

    @Transactional
    public void rollback(StockRollbackEvent stockRollbackEvent) {
        Order order = orderRepository.findByOrderId(stockRollbackEvent.getOrderId()).orElse(null);
        if (order == null) {
            stockRollbackEvent.setResult(Result.FAILED);
        } else {
            order.setResult(Result.CANCEL);
            Stock stock = order.getStock();
            stock.setCount(stock.getCount() + order.getCount());

            orderRepository.save(order);
            stockRollbackEvent.setResult(Result.SUCCESS);
        }

        sendMessage(stockRollbackEvent, "stockRollback");
    }

    private void sendMessage(SagaEvent event, String type) {
        streamBridge.send("orderSagaReplies",
                          MessageBuilder.withPayload(event)
                                  .setHeader("type", type).build());
    }
}
