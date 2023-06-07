package com.example.planetapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.planetapi.domain.Planet;

public interface PlanetRepository extends JpaRepository<Planet, Long> {

}
