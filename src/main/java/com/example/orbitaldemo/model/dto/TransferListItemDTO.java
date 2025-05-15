package com.example.orbitaldemo.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransferListItemDTO {

    private Long id;

    private TeamShortDTO sellerTeam;

    private PlayerDTO player;

    private BigDecimal price;

    private LocalDateTime createdAt;

}
