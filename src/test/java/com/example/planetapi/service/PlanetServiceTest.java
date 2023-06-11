package com.example.planetapi.service;

import static com.example.planetapi.common.PlanetsConstants.INVALID_PLANET;
import static com.example.planetapi.common.PlanetsConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import com.example.planetapi.domain.Planet;
import com.example.planetapi.repository.PlanetRepository;
import com.example.planetapi.repository.QueryBuilder;

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

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {

        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);

    }

    @Test
    public void getPlanet_ByExistingId_ReturnPlanet() {

        when(planetRepository.findById(1L)).thenReturn(Optional.of(PLANET));
        Optional<Planet> sut = planetService.getById(1L);

        assertThat(sut).isNotEmpty();

        assertThat(sut.get()).isEqualTo(PLANET);

    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnEmpty() {
        when(planetRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Planet> sut = planetService.getById(1L);

        assertThat(sut).isEmpty();
        

    }

    @Test
    public void getPlanet_ByExistingName_ReturnPlanet(){

        when(planetRepository.findByName("name")).thenReturn(Optional.of(PLANET));

        Optional<Planet> sut = planetService.findByName("name");

        assertThat(sut).isNotEmpty();

        assertThat(sut.get()).isEqualTo(PLANET);

    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnEmpty() {

        final String name = "Unexisting Name";
        when(planetRepository.findByName(name)).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.findByName(name);

        assertThat(sut).isEmpty();
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {
        List<Planet> planets = new ArrayList<>() {
            {
                add(PLANET);
            }
        };

        Example<Planet> query = QueryBuilder
                .makeQuery(Planet.builder().climate(PLANET.getClimate()).terrain(PLANET.getTerrain()).build());

        when(planetRepository.findAll(query)).thenReturn(planets);

        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).isNotEmpty().hasSize(1);
        assertThat(sut.get(0)).isEqualTo(PLANET);

    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {

        Example<Planet> query = QueryBuilder
                .makeQuery(Planet.builder().climate(PLANET.getClimate()).terrain(PLANET.getTerrain()).build());

        when(planetRepository.findAll(query)).thenReturn(Collections.emptyList());

        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).isEmpty();

    }

    @Test
    public void removePlanet_WithExistingId_doesNotThrowAnyException() {

        assertThatCode(() -> planetService.deletePlanet(1L)).doesNotThrowAnyException();

        // TODO implement
    }

    @Test
    public void removePlanet_WithUnexistingId_ThrowsException() {
        // TODO implement
        doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);

        assertThatThrownBy(() -> planetService.deletePlanet(99L)).isInstanceOf(RuntimeException.class);

    }

}
