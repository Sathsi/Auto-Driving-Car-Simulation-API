package com.gic.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutonomousCar {
    private String currentCoordinates;
    private String currentFacingDirection;
    private String commands;
    private String name;
}
