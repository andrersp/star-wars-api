package com.example.planetapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.planetapi.domain.Planet;

public interface PlanetRepository extends JpaRepository<Planet, Long> {

    Optional<Planet> findByName(String name);

    @Override
    <S extends Planet> List<S> findAll(Example<S> example);

}
