package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.OrderDto;
import org.example.entity.OrderEntity;
import org.example.entity.UserEntity;

@UtilityClass
public class OrderMapper {

    public static OrderDto toDto(OrderEntity entity) {
        if (entity == null) return null;

        return OrderDto.builder()
                .id(entity.getId())
                .product(entity.getProduct())
                .price(entity.getPrice())
                .userId(entity.getUser().getId())
                .build();
    }

    public static OrderEntity toEntity(OrderDto dto, UserEntity user) {
        if (dto == null) return null;

        return OrderEntity.builder()
                .id(dto.getId())
                .product(dto.getProduct())
                .price(dto.getPrice())
                .user(user)
                .build();
    }
}