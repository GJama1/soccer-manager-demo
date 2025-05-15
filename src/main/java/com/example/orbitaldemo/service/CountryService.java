package com.example.orbitaldemo.service;

import com.example.orbitaldemo.model.domain.database.CountryEntity;
import com.example.orbitaldemo.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryEntity getCountryById(Long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found"));
    }

    public Page<CountryEntity> getAllCountries(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

}
