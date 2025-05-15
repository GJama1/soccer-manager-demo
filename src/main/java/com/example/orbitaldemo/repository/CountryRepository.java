package com.example.orbitaldemo.repository;

import com.example.orbitaldemo.model.domain.database.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {



}
