package com.example.demo;

import com.example.demo.domain.Planet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.common.PlanetConstants.PLANET;
import static com.example.demo.common.PlanetConstants.TATOOINE;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@Sql(scripts = {"/import_planets.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PlanetIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public  void createPlanet_ReturnsCreated(){
       ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets",PLANET, Planet.class);
       Assertions.assertEquals(sut.getStatusCode(), HttpStatus.CREATED);
       Assertions.assertNotNull(sut.getBody().getId());
       Assertions.assertEquals(sut.getBody().getName(), PLANET.getName());
       Assertions.assertEquals(sut.getBody().getClimate(), PLANET.getClimate());
       Assertions.assertEquals(sut.getBody().getTerrain(), PLANET.getTerrain());


    }

    @Test
    public void  getPlanet_ReturnsPlanets(){
       ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1",Planet.class);
        Assertions.assertEquals(sut.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(sut.getBody(), TATOOINE);
    }

    @Test
    public void  getPlanetByName_ReturnsPlanets(){
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/name/"+TATOOINE.getName(),Planet.class);
        Assertions.assertEquals(sut.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(sut.getBody(), TATOOINE);

    }

    @Test
    public void  listPlanet_ReturnsAllPlanets(){
        var sut =  restTemplate.getForEntity("/planets",Planet[].class);

        Assertions.assertEquals(sut.getStatusCode(), HttpStatus.OK);
        Assertions.assertFalse(Arrays.stream(sut.getBody()).toList().isEmpty());

    }

    @Test
    public void  listPlanet_ByClimate_ReturnsPlanets(){
        ResponseEntity<Planet[]> sut =  restTemplate.getForEntity("/planets?"+String.format("climate=%s",TATOOINE.getClimate()),Planet[].class);

        Assertions.assertEquals(sut.getStatusCode(), HttpStatus.OK);
        Assertions.assertFalse(Arrays.stream(sut.getBody()).toList().isEmpty());
        Assertions.assertTrue(Arrays.stream(sut.getBody()).toList().contains(TATOOINE));
    }

    @Test
    public void  listPlanet_ByTerrain_ReturnsPlanets(){
        ResponseEntity<Planet[]> sut =  restTemplate.getForEntity("/planets?"+String.format("terrain=%s",TATOOINE.getTerrain()),Planet[].class);

        Assertions.assertEquals(sut.getStatusCode(), HttpStatus.OK);
        Assertions.assertFalse(Arrays.stream(sut.getBody()).toList().isEmpty());
        Assertions.assertTrue(Arrays.stream(sut.getBody()).toList().contains(TATOOINE));

    }

    @Test
    public void  removePlanet_ReturnsNoContent(){
        ResponseEntity<Void> sut = restTemplate.exchange("/planets/"+TATOOINE.getId(), HttpMethod.DELETE,null,Void.class);
        Assertions.assertEquals(sut.getStatusCode(), HttpStatus.NO_CONTENT);
    }
}
