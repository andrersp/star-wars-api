package com.example.planetapi.repository;

import static com.example.planetapi.common.PlanetsConstants.INVALID_PLANET;
import static com.example.planetapi.common.PlanetsConstants.PLANET;
import static com.example.planetapi.common.PlanetsConstants.PLANETS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.planetapi.domain.Planet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
public class PlanetRepositoryTest {

  @Autowired
  private PlanetRepository planetRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @AfterEach
  public void AfterEach() {
    PLANET.setId(null);
  }

  @Test
  public void createPlanet_WithValidData_ReturnsPlanet() {
    Planet planet = planetRepository.save(PLANET);

    Planet sut = testEntityManager.find(Planet.class, planet.getId());

    assertThat(sut).isNotNull();

    assertThat(sut.getName()).isEqualTo(planet.getName());
    assertThat(sut.getClimate()).isEqualTo(planet.getClimate());
  }

  @Test
  public void createPlanet_WithInvalidData_ThrowsException() {
    Planet emptyPlanet = new Planet();

    assertThatThrownBy(() -> planetRepository.save(emptyPlanet))
        .isInstanceOf(RuntimeException.class);

    assertThatThrownBy(() -> planetRepository.save(INVALID_PLANET))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  public void createPlanet_WithExistingName_ThrowsException() {
    Planet planetCreated = testEntityManager.persistFlushFind(PLANET);
    testEntityManager.detach(planetCreated);

    planetCreated.setId(null);

    assertThatThrownBy(() -> planetRepository.save(planetCreated))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  public void getPlanet_ByExistingId_ReturnPlanet() {
    Planet planet = testEntityManager.persistAndFlush(PLANET);

    Optional<Planet> sut = planetRepository.findById(planet.getId());

    assertThat(sut).isNotEmpty();
    assertThat(sut.get().getId()).isEqualTo(planet.getId());
  }

  @Test
  public void getPlanet_ByExistingId_ReturnException() {
    Optional<Planet> sut = planetRepository.findById(1L);

    assertThat(sut).isEmpty();
  }

  @Test
  void getPlanet_ByExistingName_ReturnPlanet() {
    Planet planet = testEntityManager.persistAndFlush(PLANET);

    Optional<Planet> sut = planetRepository.findByName(planet.getName());

    assertThat(sut).isNotEmpty();
    assertThat(sut.get().getId()).isEqualTo(planet.getId());
  }

  @Test
  public void getPlanet_ByInexistingName_ReturnException() {
    Optional<Planet> sut = planetRepository.findByName("name");

    assertThat(sut).isEmpty();
  }

  @Test
  @Sql("/import_planets.sql")
  public void listPlanets_ReturnsFilteredPlanets() {
    Example<Planet> query = QueryBuilder.makeQuery(Planet.builder().build());

    List<Planet> sut = planetRepository.findAll(query);

    assertThat(sut.size()).isEqualTo(3);
    assertThat(sut.get(0)).isEqualTo(PLANETS.get(0));
  }

  @Test
  @Sql("/import_planets.sql")
  public void listPlanets_ReturnsNoPlanets() {
    Example<Planet> query = QueryBuilder.makeQuery(
        Planet.builder().climate("Nenhum").terrain("nenhum").build());

    List<Planet> sut = planetRepository.findAll(query);

    assertThat(sut.size()).isEqualTo(0);
  }

  @Test
  public void removePlanet_WithExistingId_RemovesPlanetFromDatabase() {
    Planet planet = testEntityManager.persistFlushFind(PLANET);
    planetRepository.deleteById(planet.getId());

    Planet planetRemoved = testEntityManager.find(Planet.class, planet.getId());

    assertThat(planetRemoved).isNull();
  }

}
