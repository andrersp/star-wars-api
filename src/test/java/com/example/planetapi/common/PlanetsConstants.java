package com.example.planetapi.common;

import com.example.planetapi.domain.Planet;

public class PlanetsConstants {
    public static final Planet PLANET = Planet.builder().name("name").climate("climate").terrain("terrains").build();
}
