package com.example.orbitaldemo.model.dto;

import com.example.orbitaldemo.model.enums.PlayerPosition;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlayerDTO {

    private Long id;

    private String firstname;

    private String lastname;

    private PlayerPosition position;

    private Integer age;

    private BigDecimal marketValue;

}
