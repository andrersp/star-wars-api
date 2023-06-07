package com.example.planetapi.service;

import org.springframework.stereotype.Service;

import com.example.planetapi.domain.Planet;
import com.example.planetapi.repository.PlanetRepository;

@Service
public class PlanetService {

    private PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Planet create(Planet planet) {

        return planetRepository.save(planet);

    }

}
