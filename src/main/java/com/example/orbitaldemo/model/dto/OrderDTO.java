package com.example.orbitaldemo.model.dto;

import com.example.orbitaldemo.model.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderDTO {

    private Long id;

    private PlayerShortDTO player;

    private BigDecimal price;

    private OrderStatus status;

    private LocalDateTime createdAt;

}
