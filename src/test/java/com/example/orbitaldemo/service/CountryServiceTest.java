package com.example.orbitaldemo.service;

import com.example.orbitaldemo.model.domain.database.CountryEntity;
import com.example.orbitaldemo.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCountryById_ShouldReturnCountry_WhenFound() {
        Long countryId = 1L;
        CountryEntity country = new CountryEntity();
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));
        CountryEntity result = countryService.getCountryById(countryId);
        assertNotNull(result);
        assertEquals(country, result);
        verify(countryRepository).findById(countryId);
    }

    @Test
    void getCountryById_ShouldThrowException_WhenNotFound() {
        Long countryId = 1L;
        when(countryRepository.findById(countryId)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> countryService.getCountryById(countryId));
        assertEquals("404 NOT_FOUND \"Country not found\"", exception.getMessage());
        verify(countryRepository).findById(countryId);
    }

    @Test
    void getAllCountries_ShouldReturnPagedCountries() {
        Pageable pageable = Pageable.unpaged();
        Page<CountryEntity> page = new PageImpl<>(Collections.emptyList());
        when(countryRepository.findAll(pageable)).thenReturn(page);
        Page<CountryEntity> result = countryService.getAllCountries(pageable);
        assertNotNull(result);
        assertEquals(page, result);
        verify(countryRepository).findAll(pageable);
    }

}