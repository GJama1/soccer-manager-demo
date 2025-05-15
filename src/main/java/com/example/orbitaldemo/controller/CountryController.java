package com.example.orbitaldemo.controller;

import com.example.orbitaldemo.facade.CountryFacade;
import com.example.orbitaldemo.model.dto.CountryDTO;
import com.example.orbitaldemo.model.dto.PagedDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryFacade countryFacade;

    @GetMapping
    public ResponseEntity<PagedDTO<CountryDTO>> getAllCountries(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(countryFacade.getAllCountries(page, size));
    }

}
