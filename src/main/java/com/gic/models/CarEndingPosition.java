package com.gic.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
public class CarEndingPosition {
    private int x;
    private int y;
    private char direction;

}
