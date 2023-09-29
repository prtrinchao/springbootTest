package com.example.demo.Repository;

import  static  com.example.demo.common.PlanetConstants.PLANET;
import static com.example.demo.common.PlanetConstants.TATOOINE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.demo.domain.Planet;
import com.example.demo.domain.QueryBuilder;
import com.example.demo.repository.PlanetReposity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Optional;


@DataJpaTest
public class PlanetRepositoryTest {
    @Autowired
    private PlanetReposity planetReposity;
    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public  void afterEach(){
        PLANET.setId(null);
    }

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet(){
      //Arrange
       Planet planet =  planetReposity.save(PLANET);
        //Act
       Planet sut = testEntityManager.find(Planet.class,planet.getId());

        //Assert
        Assertions.assertNotNull(sut);
        Assertions.assertEquals(sut.getName(),PLANET.getName());
        Assertions.assertEquals(sut.getTerrain(),PLANET.getTerrain());
        Assertions.assertEquals(sut.getClimate(),PLANET.getClimate());

    }

    @Test
    public  void createPlanet_WithInvalidData_ThrowsExceptions(){
        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("","","");

        Assertions.assertThrows(RuntimeException.class, (()-> planetReposity.save(emptyPlanet)));
        Assertions.assertThrows(RuntimeException.class, (()-> planetReposity.save(invalidPlanet)));
    }

    @Test
    public void  createPlanet_WithExistingName_ThrowsException(){
       Planet planet = testEntityManager.persistFlushFind(PLANET);
       testEntityManager.detach(planet);
       planet.setId(null);
        Assertions.assertThrows(RuntimeException.class, (()-> planetReposity.save(planet)));

    }

    @Test
    public  void getPlanet_ByExistingId_ReturnsPlanet(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> planetOptional = planetReposity.findById(planet.getId());

        Assertions.assertTrue(planetOptional.isPresent());
        Assertions.assertEquals(planetOptional.get(),PLANET);

    }

    @Test
    public void  getPlanet_ByUnexistingId_ReturnsEmpty(){
        Optional<Planet> planetOptional = planetReposity.findById(1L);
        Assertions.assertTrue(planetOptional.isEmpty());
    }

    @Test
    public  void getPlanet_ByExistingName_ReturnsPlanet(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> planetOptional = planetReposity.findByName(planet.getName());

        Assertions.assertTrue(planetOptional.isPresent());
        Assertions.assertEquals(planetOptional.get(),PLANET);

    }

    @Test
    public void  getPlanet_ByUnexistingName_ReturnsEmpty(){
        Optional<Planet> planetOptional = planetReposity.findByName("name");
        Assertions.assertTrue(planetOptional.isEmpty());
    }

    @Sql(scripts = "/import_planets.sql" )
    @Test
    public void listPlanets_ReturnsFilteredPlanets(){
        Example<Planet> queryWithoutFilter = QueryBuilder.makeQuery(new Planet());
        Example<Planet> queryWithFilter = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(),TATOOINE.getTerrain()));

        List<Planet> responseWithoutFilters = planetReposity.findAll(queryWithoutFilter);
        List<Planet> responseWithFilters = planetReposity.findAll(queryWithFilter);

        Assertions.assertEquals(responseWithoutFilters.size(),3);
        Assertions.assertEquals(responseWithFilters.size(),1);
        Assertions.assertEquals(responseWithFilters.get(0),TATOOINE);

    }


    @Test
    public void listPlanets_ReturnsNoPlanets(){
        Example<Planet> query = QueryBuilder.makeQuery(new Planet());
        List<Planet> response = planetReposity.findAll(query);

        Assertions.assertTrue(response.isEmpty());

    }

    @Test
    public  void removePlanet_WithExistingId_RemovesPlanetFromDatabase(){
        Planet planet = testEntityManager.persistFlushFind(PLANET);
        planetReposity.deleteById(planet.getId());

        Planet removedPlanet = testEntityManager.find(Planet.class,planet.getId());
        Assertions.assertNull(removedPlanet);


    }

    @Test
    public  void removePlanet_WithUnexistingId_RemovesPlanetFromDatabase(){
        planetReposity.deleteById(1L);
      //  Assertions.assertThrows(EmptyResultDataAccessException.class, (()-> planetReposity.deleteById(1L)));

    }
}
