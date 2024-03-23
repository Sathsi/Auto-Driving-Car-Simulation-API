package com.gic.controller;

import com.gic.models.CarCollisionResponse;
import com.gic.models.CarInputDetails;
import com.gic.models.CarEndingPosition;
import com.gic.models.CarInputRequest;
import com.gic.service.AutoDriveCarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/autoDriveCar")
@Api(tags = "Auto Drive Car Simulation")
public class AutoDriveCarController {

    private final AutoDriveCarService autoDriveCarService;

    public AutoDriveCarController(AutoDriveCarService autoDriveCarService) {
        this.autoDriveCarService = autoDriveCarService;
    }

    @ApiOperation(value = "Get the ending position and facing direction of the car", notes = "Enter input details of the current car position and field size")
    @RequestMapping(value = "/endingPosition", method = RequestMethod.POST)
    public ResponseEntity<Object> getCarEndingPositionAndDirection(@Valid final @RequestBody CarInputDetails carInputDetails) throws Exception{

        final CarEndingPosition carEndingPosition = autoDriveCarService.getCarEndingPositionAndDirection(carInputDetails);
        return new ResponseEntity<Object>(carEndingPosition, HttpStatus.OK);
    }

    @ApiOperation(value = "Ensure the collision happen between multiple cars in same field", notes = "Enter input details of the current car position and field size")
    @RequestMapping(value = "/checkCollision", method = RequestMethod.POST)
    public ResponseEntity<Object> isCarCollide(@Valid final @RequestBody CarInputRequest carInputRequest) throws Exception{

        final String collisionResponse = autoDriveCarService.isCarCollisionHappen(carInputRequest);
        return new ResponseEntity<Object>(collisionResponse, HttpStatus.OK);
    }

}
