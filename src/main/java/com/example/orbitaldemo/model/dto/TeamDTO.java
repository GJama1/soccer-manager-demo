package com.example.orbitaldemo.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TeamDTO {

    private Long id;

    private String name;

    private CountryDTO country;

    private BigDecimal teamValue;

    private BigDecimal budget;

}
