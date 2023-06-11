package com.example.planetapi.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.example.planetapi.domain.Planet;

public class QueryBuilder {
    private QueryBuilder() {
        throw new IllegalStateException();
    }

    public static Example<Planet> makeQuery(Planet planet) {

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
        return Example.of(planet, exampleMatcher);

    }
}
