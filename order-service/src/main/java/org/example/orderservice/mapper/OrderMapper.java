package org.example.orderservice.mapper;

import org.example.orderservice.dto.OrderDTO;
import org.example.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Order map(OrderDTO dto);

    @Mapping(target = "orderId", source = "order.id")
    OrderDTO map(Order order);
}
