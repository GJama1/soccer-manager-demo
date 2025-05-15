package com.example.orbitaldemo.model.mapper;

import com.example.orbitaldemo.model.domain.database.CountryEntity;
import com.example.orbitaldemo.model.dto.CountryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryMapper {

    public static CountryDTO toCountryDTO(CountryEntity country) {
        return CountryDTO.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }

}
