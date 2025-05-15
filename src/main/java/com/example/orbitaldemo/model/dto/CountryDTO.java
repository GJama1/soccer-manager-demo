package com.example.orbitaldemo.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryDTO {

    private Long id;

    private String name;

}
