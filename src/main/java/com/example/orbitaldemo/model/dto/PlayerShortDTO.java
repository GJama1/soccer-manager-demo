package com.example.orbitaldemo.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerShortDTO {

    private Long id;

    private String firstname;

    private String lastname;

}
