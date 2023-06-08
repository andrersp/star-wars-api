package com.example.planetapi.service;

import static com.example.planetapi.common.PlanetsConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.planetapi.domain.Planet;
import com.example.planetapi.repository.PlanetRepository;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    // @Autowired
    @InjectMocks
    private PlanetService planetService;

    // @MockBean
    @Mock
    private PlanetRepository planetRepository;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {

        // AAA
        // Arange
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        // System under test
        // Act
        Planet sut = planetService.create(PLANET);

        // Assert
        assertThat(sut).isEqualTo(PLANET);

    }

}
