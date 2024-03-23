package com.gic.service.impl;

import com.gic.models.CarCollisionResponse;
import com.gic.models.CarInputDetails;
import com.gic.models.CarEndingPosition;
import com.gic.models.CarInputRequest;
import com.gic.models.AutonomousCar;
import com.gic.service.AutoDriveCarService;
import com.gic.utils.common.Command;
import com.gic.utils.validationconstraints.RequestValidator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AutoDriveCarServiceImpl implements AutoDriveCarService {

    private RequestValidator requestValidator;

    public AutoDriveCarServiceImpl(RequestValidator requestValidator) {
        this.requestValidator = requestValidator;
    }

    @Override
    public CarEndingPosition getCarEndingPositionAndDirection(CarInputDetails carInputDetails) throws Exception {

        requestValidator.validateCarAutoDriveInputDetails(carInputDetails.getCurrentCoordinates(),
                carInputDetails.getCurrentFacingDirection(),carInputDetails.getCommands());

        Map<String, Integer> coordinates = getCarCoordinate(carInputDetails.getCurrentCoordinates().split(","));

        CarEndingPosition carEndingPosition = calculateCarEndingPosition(carInputDetails.getFieldDimension().getWidth(),
                carInputDetails.getFieldDimension().getHeight(), coordinates.get("x"),coordinates.get("y"),
                carInputDetails.getCurrentFacingDirection(),
                carInputDetails.getCommands());


        return carEndingPosition;
    }

    @Override
    public String isCarCollisionHappen(CarInputRequest carInputRequest) throws Exception {

        requestValidator.validateCarAutoDriveInputDetails(carInputRequest.getCarInputDetailsList());

        CarCollisionResponse carCollisionResponse = null;

        //Find the maximum command length among all cars. Max length is consider as max driving time
        int drivingTime = carInputRequest.getCarInputDetailsList().stream()
                .mapToInt(car -> car.getCommands().length())
                .max()
                .orElse(0);

        //As a improvement this can be generated using factory design pattern
        AutonomousCar carOne = new AutonomousCar();
        AutonomousCar carTwo = new AutonomousCar();

        if (carInputRequest.getCarInputDetailsList().size() >= 2) {
            carOne = carInputRequest.getCarInputDetailsList().get(0);
            carTwo = carInputRequest.getCarInputDetailsList().get(1);
        }

        Map<String, Integer> startCoordinatesCar1 = getCarCoordinate(carOne.getCurrentCoordinates().split(","));
        Map<String, Integer> startCoordinatesCar2 = getCarCoordinate(carTwo.getCurrentCoordinates().split(","));

        CarEndingPosition carOnePosition = getCarPosition(carOne, startCoordinatesCar1);
        CarEndingPosition carTwoPosition = getCarPosition(carTwo, startCoordinatesCar2);

        // Check for collision with other cars
        for(int i=0; i < drivingTime; i++){
            carOnePosition = calculateCarEndingPosition(carInputRequest.getFieldDimension().getWidth()
                    ,carInputRequest.getFieldDimension().getHeight()
                    ,carOnePosition.getX()
                    ,carOnePosition.getY()
                    ,String.valueOf(carOnePosition.getDirection())
                    ,String.valueOf(carOne.getCommands().toCharArray()[i]));

            carTwoPosition = calculateCarEndingPosition(carInputRequest.getFieldDimension().getWidth()
                    ,carInputRequest.getFieldDimension().getHeight()
                    ,carTwoPosition.getX()
                    ,carTwoPosition.getY()
                    ,String.valueOf(carTwoPosition.getDirection())
                    ,String.valueOf(carTwo.getCommands().toCharArray()[i]));

            if(carOnePosition.getX() == carTwoPosition.getX() &&
                    carOnePosition.getY() == carTwoPosition.getY()){
                carCollisionResponse = CarCollisionResponse.builder()
                        .carNames(carOne.getName() + " " + carTwo.getName())
                        .collisionPosition(carOnePosition.getX() + " " + carOnePosition.getY())
                        .step(i+1)
                        .build();
                //log got result
                break;
            }
        }
        return getCollisionResponse(carCollisionResponse);

    }

    private CarEndingPosition getCarPosition(AutonomousCar car, Map<String, Integer> startCoordinatesCar){
        return CarEndingPosition.builder().x(startCoordinatesCar.get("x"))
                .y(startCoordinatesCar.get("y")).direction(car.getCurrentFacingDirection().charAt(0)).build();
    }

    //Generate the final result from the operation
    private String getCollisionResponse(CarCollisionResponse carCollisionResponse){
        String result = "No Collision";
        if(carCollisionResponse != null){
            StringBuilder stringBuilder = new StringBuilder();
            result = stringBuilder.append(carCollisionResponse.getCarNames())
                    .append("\n")
                    .append(carCollisionResponse.getCollisionPosition())
                    .append("\n")
                    .append(carCollisionResponse.getStep()).toString();

        }
        return result;

    }

    //Calculate the car ending position
    private CarEndingPosition calculateCarEndingPosition(int width, int height,
                                                         int startXCoord, int startYCoord,
                                                         String startDirection, String commands){
        int[] xCoord = {0, 1, 0, -1};
        int[] yCoord = {1, 0, -1, 0};
        AtomicInteger x = new AtomicInteger(startXCoord);
        AtomicInteger y = new AtomicInteger(startYCoord);
        AtomicInteger facingDirectionIndex = new AtomicInteger(getIndex(startDirection.toUpperCase().charAt(0)));

        commands.chars().forEach(command -> {
            if(Character.toUpperCase(command) == Command.L.name().charAt(0)){
                facingDirectionIndex.set((facingDirectionIndex.get() + 3) % 4);

            } else if(Character.toUpperCase(command) == Command.R.name().charAt(0)){
                facingDirectionIndex.set((facingDirectionIndex.get() + 1) % 4);

            } else if(Character.toUpperCase(command) == Command.F.name().charAt(0)) {
                int newXCoordinate = x.get() + xCoord[facingDirectionIndex.get()];
                int newYCoordinate = y.get() + yCoord[facingDirectionIndex.get()];

                // Check boundary conditions
                if(newXCoordinate >= 0 && newXCoordinate <= width
                        && newYCoordinate >= 0 && newYCoordinate <= height){
                    x.set(newXCoordinate);
                    y.set(newYCoordinate);
                }

            }
        });

        CarEndingPosition carEndingPosition = CarEndingPosition.builder()
                .x(x.get())
                .y(y.get())
                .direction(getDirection(facingDirectionIndex.get()))
                .build();

        return carEndingPosition;

    }

    private static int getIndex(char direction) {
        switch (direction) {
            case 'N':
                return 0;
            case 'E':
                return 1;
            case 'S':
                return 2;
            case 'W':
                return 3;
            default:
                return -1; // Invalid direction
        }
    }

    private static char getDirection(int index) {
        char[] directions = {'N', 'E', 'S', 'W'};
        return directions[index];
    }

    private Map<String, Integer> getCarCoordinate(String[] startCoordinates){
        Map<String, Integer> positions = new HashMap<>();
        positions.put("x", Integer.parseInt(startCoordinates[0]));
        positions.put("y", Integer.parseInt(startCoordinates[1]));
        return positions;
    }


}
