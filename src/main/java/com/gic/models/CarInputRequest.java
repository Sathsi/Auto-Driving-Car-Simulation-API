package com.gic.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarInputRequest {
    private Dimension fieldDimension;
    private List<AutonomousCar> carInputDetailsList;
}
