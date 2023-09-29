package com.example.demo.service;

import com.example.demo.domain.Planet;
import com.example.demo.domain.QueryBuilder;
import com.example.demo.repository.PlanetReposity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.common.PlanetConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

    @InjectMocks
    private  PlanetService planetService;
    @Mock
    private PlanetReposity planetReposity;


    @Test
    public void createPlanet_WithValidDate_ReturnsPlanet(){
      //Arrange
       when(planetReposity.save(PLANET)).thenReturn(PLANET);
     //Act
        // system under test
      Planet sut = planetService.create(PLANET);

      // Assert
      Assertions.assertEquals(sut,PLANET);

    }

    @Test
    public  void createPlanet_WithInvalidData_ThrowsException(){
        //Arrange
        when(planetReposity.save(INVALID_PLANET)).thenThrow(RuntimeException.class);
        //Asset
        Assertions.assertThrows(RuntimeException.class, (()-> planetService.create(INVALID_PLANET)));

    }


    @Test
    public  void getPlanet_ByExistingId_ReturnsPlanet(){
        //Arrange
        when(planetReposity.findById(PLANET_ID)).thenReturn(Optional.of(PLANET));

        //Act
        // system under test
        var sut = planetService.get(PLANET_ID);

        // Assert
        Assertions.assertNotNull(sut);
        Assertions.assertEquals(sut.get(),PLANET);

    }

    @Test
    public  void getPlanet_ByUnexistingId_ReturnsEmpty(){
        //Arrange
        when(planetReposity.findById(PLANET_ID_UNEXISTING)).thenReturn(Optional.empty());

        //Act
        // system under test
        var sut = planetService.get(PLANET_ID_UNEXISTING);

        // Assert
        Assertions.assertTrue(sut.isEmpty());

    }

    @Test
    public  void getPlanet_ByExistingName_ReturnsPlanet(){
        //Arrange
        when(planetReposity.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        //Act
        // system under test
        var sut = planetService.getByName(PLANET.getName());

        // Assert
        Assertions.assertNotNull(sut);
        Assertions.assertEquals(sut.get(),PLANET);

    }

    @Test
    public  void getPlanet_ByUnexistingName_ReturnsEmpty(){
        //Arrange

        when(planetReposity.findByName("name")).thenReturn(Optional.empty());

        //Act
        // system under test
        var sut = planetService.getByName("name");

        // Assert
        Assertions.assertTrue(sut.isEmpty());

    }

    @Test
    public void listPlanets_ReturnsAllPlanets(){
        //Arrange
        List<Planet> list = new ArrayList<Planet>(){{
            add(PLANET);
        }};
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(),PLANET.getTerrain()));

        when(planetReposity.findAll(query)).thenReturn(list);

        //Act
        var sut = planetService.list(PLANET.getTerrain(),PLANET.getClimate());

        //Assert
        Assertions.assertNotNull(sut);
        Assertions.assertEquals(sut.size(),1);
        Assertions.assertEquals(sut.get(0),PLANET);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets(){
     //   Example<Planet> query = QueryBuilder.makeQuery(any());
        when(planetReposity.findAll(any(Example.class))).thenReturn(Collections.emptyList());

        //Act
        var sut = planetService.list(PLANET.getTerrain(),PLANET.getClimate());

        //Assert
        Assertions.assertTrue(sut.isEmpty());
    }

    @Test
    public  void removePlanet_WithExistingId_doesNotThrowAnyException(){

        when(planetReposity.findById(PLANET_ID)).thenReturn(Optional.of(TATOOINE));
        Assertions.assertDoesNotThrow(()-> planetService.remove(PLANET_ID));

    }

    @Test
    public void removePlanet_WithUnexistingId_ThrowsException(){
        when(planetReposity.findById(PLANET_ID)).thenReturn(Optional.of(TATOOINE));
        doThrow(new RuntimeException()).when(planetReposity).deleteById(anyLong());
        Assertions.assertThrows(RuntimeException.class, (()-> planetService.remove(anyLong())));


    }


}
