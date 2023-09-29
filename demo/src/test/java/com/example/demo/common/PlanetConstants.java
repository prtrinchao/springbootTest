package com.example.demo.common;

import com.example.demo.domain.Planet;
import com.example.demo.service.PlanetService;

import java.util.ArrayList;
import java.util.List;

public class PlanetConstants {
    public static final Planet PLANET = new Planet("name","climate","terrain");
    public static final Planet INVALID_PLANET = new Planet("","","");
    public  static  final long PLANET_ID = 1;
    public  static  final long PLANET_ID_UNEXISTING = 0;

    public static final Planet TATOOINE = new Planet(1L,"Tatooine","arid","desert");
    public static final Planet ALDERAAN = new Planet(2L,"Alderaan","temperate","grasslands,mountains");
    public static final Planet YAVINIV = new Planet(3L,"Yavin IV","temperate, tropical","jungle,rainfores");
    public static final List<Planet> PLANETS = new ArrayList<>(){
        {
            add(TATOOINE);
            add(ALDERAAN);
            add(YAVINIV);
         }
    };
}
