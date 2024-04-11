package org.example.deliveryservice.mapper;

import org.example.deliveryservice.dto.DeliveryDTO;
import org.example.deliveryservice.entity.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {
    @Mapping(target = ".", source = "delivery")
    DeliveryDTO map(Delivery delivery);
}
