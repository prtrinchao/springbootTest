package com.example.demo.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "planets")
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private  String name;

    @NotEmpty
    @Column(nullable = false)
    private String climate;

    @NotEmpty
    @Column(nullable = false)
    private String terrain;

    public  Planet(String name, String climate , String terrain){
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public  Planet( String climate , String terrain){
        this.climate = climate;
        this.terrain = terrain;
    }

    @Override
    public boolean equals(Object obj) {
       return EqualsBuilder.reflectionEquals(obj,this);

    }


}
