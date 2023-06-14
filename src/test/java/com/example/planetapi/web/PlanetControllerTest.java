package com.example.planetapi.web;

import static com.example.planetapi.common.PlanetsConstants.INVALID_PLANET;
import static com.example.planetapi.common.PlanetsConstants.PLANET;
import static com.example.planetapi.common.PlanetsConstants.PLANETS;
import static com.example.planetapi.common.PlanetsConstants.TATOOINE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.planetapi.domain.Planet;
import com.example.planetapi.service.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private PlanetService planetService;

  @Test
  public void createPlanet_WithValidData_ReturnsCreated() throws Exception {
    when(planetService.create(PLANET)).thenReturn(PLANET);

    mockMvc
      .perform(
        post("/planets")
          .content(objectMapper.writeValueAsString(PLANET))
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$").value(PLANET));
  }

  @Test
  public void createPlanet_WithInvalidData_ReturnsBadRequest()
      throws Exception {
    Planet emptyPlanet = new Planet();

    mockMvc
        .perform(
            post("/planets")
                .content(objectMapper.writeValueAsString(emptyPlanet))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity());

    mockMvc
        .perform(
            post("/planets")
                .content(objectMapper.writeValueAsString(INVALID_PLANET))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void createPlanet_WithExistingName_ReturnConflict() throws Exception {
    when(planetService.create(any()))
      .thenThrow(DataIntegrityViolationException.class);

    mockMvc
      .perform(
        post("/planets")
          .content(objectMapper.writeValueAsString(PLANET))
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isConflict());
  }

  @Test
  public void getPlanet_ByExistingId_ReturnPlanet() throws Exception {
    when(planetService.getById(1L)).thenReturn(Optional.of(PLANET));

    mockMvc
      .perform(get("/planets/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").value(PLANET));
  }

  @Test
  public void getPlanet_ByExistingId_ReturnNotFound() throws Exception {
    mockMvc.perform(get("/planets/1")).andExpect(status().isNotFound());
  }

  @Test
  void getPlanet_ByExistingName_ReturnPlanet() throws Exception {
    when(planetService.findByName("name")).thenReturn(Optional.of(PLANET));

    mockMvc
      .perform(get("/planets/name/name"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").value(PLANET));
  }

  @Test
  public void getPlanet_ByInexistingName_ReturnNotFound() throws Exception {
    mockMvc.perform(get("/planets/name/name")).andExpect(status().isNotFound());
  }

  @Test
  public void listPlanets_ReturnsFilteredPlanets() throws Exception {

    when(planetService.list(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));


    mockMvc
      .perform(get("/planets?" + String.format("terrain=%s&climate=%s", TATOOINE.getTerrain(), TATOOINE.getClimate())))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", Matchers.hasSize(1)))
      .andExpect(jsonPath("$[0]").value(TATOOINE));
  }

  @Test
  public void listPlanets_ReturnsAllPlanets() throws Exception {

    when(planetService.list(null, null )).thenReturn(PLANETS);
    
    mockMvc
      .perform(get("/planets?"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", Matchers.hasSize(3)));
  }

  @Test
  public void listPlanets_ReturnsNoPlanets() throws Exception {
    when(planetService.list(null, null )).thenReturn(Collections.emptyList());
    
    mockMvc
      .perform(get("/planets"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", Matchers.hasSize(0)));
  }

  @Test
  public void removePlanet_WithExistingId_ReturnsNoContent() throws Exception {

    mockMvc.perform(delete("/planets/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  public void removePlanet_WithUnexistingId_ReturnsNotFound() throws Exception {

    final int planetID = 1;
    doThrow(new EmptyResultDataAccessException(planetID)).when(planetService).deletePlanet(1L);
    mockMvc.perform(delete("/planets/1"))
        .andExpect(status().isNotFound());

  }

}
