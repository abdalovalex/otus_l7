package org.example.orderservice.dto;

import lombok.Data;

@Data
public class ErrorDto {
    private Integer code;
    private String message;
}
