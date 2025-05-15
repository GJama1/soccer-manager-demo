package com.example.orbitaldemo.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferListFilterRequestDTO {

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Integer minAge;

    private Integer maxAge;

    private String searchText;

}
