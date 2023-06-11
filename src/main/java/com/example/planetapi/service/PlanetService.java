package com.example.planetapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.example.planetapi.domain.Planet;
import com.example.planetapi.repository.PlanetRepository;
import com.example.planetapi.repository.QueryBuilder;

@Service
public class PlanetService {

    private PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Planet create(Planet planet) {

        return planetRepository.save(planet);

    }

    public Optional<Planet> getById(Long id) {

        return planetRepository.findById(id);

    }

    public Optional<Planet> findByName(String name) {

        return planetRepository.findByName(name);

    }

    public List<Planet> list(String terrain, String climate) {
        Example<Planet> query = QueryBuilder.makeQuery(Planet.builder().terrain(terrain).climate(climate).build());

        return planetRepository.findAll(query);
    }

    public void deletePlanet(Long id) {
        planetRepository.deleteById(id);

    }

}
