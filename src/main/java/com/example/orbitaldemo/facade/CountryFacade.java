package com.example.orbitaldemo.facade;

import com.example.orbitaldemo.model.dto.CountryDTO;
import com.example.orbitaldemo.model.dto.PagedDTO;
import com.example.orbitaldemo.model.mapper.CountryMapper;
import com.example.orbitaldemo.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryFacade {

    private final CountryService countryService;

    public PagedDTO<CountryDTO> getAllCountries(int page, int size) {
        return PagedDTO.of(
                countryService.getAllCountries(PageRequest.of(page, size)).map(CountryMapper::toCountryDTO)
        );
    }

}
