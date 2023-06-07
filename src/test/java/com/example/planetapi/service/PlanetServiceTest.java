package com.example.planetapi.service;

import static com.example.planetapi.common.PlanetsConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.planetapi.domain.Planet;

@SpringBootTest(classes = PlanetService.class)
public class PlanetServiceTest {

    @Autowired
    private PlanetService planetService;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {

        // System under test
        Planet sut = planetService.create(PLANET);
        assertThat(sut).isEqualTo(PLANET);

    }

}
