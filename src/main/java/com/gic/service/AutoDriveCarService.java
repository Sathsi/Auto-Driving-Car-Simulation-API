package com.gic.service;

import com.gic.models.CarCollisionResponse;
import com.gic.models.CarInputDetails;
import com.gic.models.CarEndingPosition;
import com.gic.models.CarInputRequest;

public interface AutoDriveCarService {

    CarEndingPosition getCarEndingPositionAndDirection(CarInputDetails carInputDetails) throws Exception;

    String isCarCollisionHappen(CarInputRequest carInputRequest) throws Exception;
}
