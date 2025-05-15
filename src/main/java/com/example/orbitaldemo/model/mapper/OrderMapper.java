package com.example.orbitaldemo.model.mapper;

import com.example.orbitaldemo.model.domain.database.OrderEntity;
import com.example.orbitaldemo.model.dto.OrderDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

    public static OrderDTO toOrderDTO(OrderEntity order) {
        return OrderDTO.builder()
                .id(order.getId())
                .player(PlayerMapper.toPlayerShortDTO(order.getListing().getPlayer()))
                .price(order.getPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

}
