package com.example.planetapi.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.planetapi.domain.Planet;
import com.example.planetapi.service.PlanetService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody @Valid Planet planet) {

        Planet newPlanet = planetService.create(planet);

        return ResponseEntity.status(HttpStatus.CREATED).body(newPlanet);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> get(@PathVariable("id") Long id) {
        return planetService.getById(id).map(planet -> ResponseEntity.status(HttpStatus.OK).body(planet))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> getByName(@PathVariable("name") String name) {

        return planetService.findByName(name).map(planet -> ResponseEntity.ok().body(planet))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping
    public ResponseEntity<List<Planet>> list(@RequestParam(required = false) String terrain,
            @RequestParam(required = false) String climate) {
        List<Planet> response = planetService.list(terrain, climate);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<?> deletePlanet(@PathVariable("id") Long id) {
        planetService.deletePlanet(id);
        return ResponseEntity.noContent().build();
    }

}
