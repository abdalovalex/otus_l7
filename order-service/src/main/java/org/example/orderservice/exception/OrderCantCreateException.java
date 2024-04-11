package org.example.orderservice.exception;

public class OrderCantCreateException extends Exception {
    public OrderCantCreateException(String message) {
        super(message);
    }
}
