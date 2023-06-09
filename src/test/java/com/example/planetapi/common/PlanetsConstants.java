package com.example.planetapi.common;

import com.example.planetapi.domain.Planet;

public class PlanetsConstants {
    public static final Planet PLANET = Planet.builder().name("name").climate("climate").terrain("terrains").build();
    public static final Planet INVALID_PLANET = Planet.builder().name("").climate("").terrain("").build();
}
