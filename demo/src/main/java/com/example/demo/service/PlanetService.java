package com.example.demo.service;

import com.example.demo.domain.Planet;
import com.example.demo.domain.QueryBuilder;
import com.example.demo.repository.PlanetReposity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetService {
    private final PlanetReposity planetReposity;
    public PlanetService(PlanetReposity planetReposity) {
        this.planetReposity = planetReposity;
    }


    public Planet create(Planet planet){
        return  planetReposity.save(planet);

    }

    public  Optional<Planet> get(Long id){
      return planetReposity.findById(id);
    }

    public  Optional<Planet> getByName(String name){
        return planetReposity.findByName(name);
    }

    public List<Planet> list(String terrain, String climate) {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(climate,terrain));
        return  planetReposity.findAll(query);
    }

    public void remove(Long id){
        var planetOptional = planetReposity.findById(id);

       if(planetOptional.isPresent()) {
          planetReposity.deleteById(id);

       }
       else {
           throw new EmptyResultDataAccessException(1);
       }
    }
}
