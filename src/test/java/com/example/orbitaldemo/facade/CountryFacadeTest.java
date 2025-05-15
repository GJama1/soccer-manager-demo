package com.example.orbitaldemo.facade;

import com.example.orbitaldemo.model.domain.database.CountryEntity;
import com.example.orbitaldemo.model.dto.CountryDTO;
import com.example.orbitaldemo.model.dto.PagedDTO;
import com.example.orbitaldemo.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CountryFacadeTest {

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountryFacade countryFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCountries_ShouldReturnPagedCountries() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CountryEntity> pagedCountries = new PageImpl<>(Collections.emptyList());
        when(countryService.getAllCountries(pageRequest)).thenReturn(pagedCountries);

        PagedDTO<CountryDTO> result = countryFacade.getAllCountries(page, size);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        verify(countryService).getAllCountries(pageRequest);
    }

}