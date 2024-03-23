package com.gic.utils.validationconstraints;

import com.gic.exception.CarInputDetailValidationException;
import com.gic.models.AutonomousCar;
import com.gic.utils.common.Command;
import com.gic.utils.common.Direction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class RequestValidator {

    public void validateCarAutoDriveInputDetails(String currentCoordinates, String currentFacingDirection,
                                                 String commands) throws Exception{
        validateStartCoordinate(currentCoordinates);
        validateStartDirection(currentFacingDirection);
        validateCommands(commands);

    }

    public void validateCarAutoDriveInputDetails (List<AutonomousCar> carInputDetailsList) throws Exception {
        if(!(carInputDetailsList.size() > 1)){
            throw new CarInputDetailValidationException(ValidationConst.NO_MULTIPLE_CARS,
                    ValidationConst.NO_MULTIPLE_CARS.message());
        }
        for (AutonomousCar carInput : carInputDetailsList) {
            validateCarAutoDriveInputDetails(carInput.getCurrentCoordinates(),
                    carInput.getCurrentFacingDirection(), carInput.getCommands());
        }
    }

    private void validateStartCoordinate (String currentCoordinates) throws Exception {
        if(isBlank(currentCoordinates)){
            throw new CarInputDetailValidationException(ValidationConst.INVALID_COORDINATES,
                    ValidationConst.INVALID_COORDINATES.message());
        }
        String[] startCoordinates = currentCoordinates.split(",");

        if(startCoordinates.length != 2){
            throw new CarInputDetailValidationException(ValidationConst.INSUFFICIENT_COORDINATES,
                    ValidationConst.INSUFFICIENT_COORDINATES.message());
        }
        AtomicBoolean isDigit = new AtomicBoolean(false);
        Arrays.stream(startCoordinates).forEach(coordinate -> {
            if(coordinate.matches("\\d+")){
                isDigit.set(true);
            }
        });

        if(!isDigit.get()){
            throw new CarInputDetailValidationException(ValidationConst.INVALID_START_COORDINATES,
                    ValidationConst.INVALID_START_COORDINATES.message());
        }
    }

    private void validateStartDirection(String startDirection) throws Exception {
        if(isBlank(startDirection)){
            throw new CarInputDetailValidationException(ValidationConst.NULL_DIRECTION,
                    ValidationConst.NULL_DIRECTION.message());
        }
        if(!Arrays.stream(Direction.values()).map(Enum::name).anyMatch(startDirection.toUpperCase()::equals)){
            throw new CarInputDetailValidationException(ValidationConst.INVALID_DIRECTION,
                    ValidationConst.INVALID_DIRECTION.message());
        }
    }

    private void validateCommands(String commands) throws Exception {
        if(isBlank(commands)){
            throw new CarInputDetailValidationException(ValidationConst.NULL_COMMAND,
                    ValidationConst.NULL_COMMAND.message());
        }
        for (char command : commands.toCharArray()) {
            if (!Arrays.stream(Command.values())
                    .map(Enum::name)
                    .anyMatch(String.valueOf(command).toUpperCase()::equals)) {
                throw new CarInputDetailValidationException(ValidationConst.INVALID_COMMAND,
                        ValidationConst.INVALID_COMMAND.message());
            }
        }

    }

    private boolean isBlank(String text){
        return StringUtils.isBlank(text);
    }

}
