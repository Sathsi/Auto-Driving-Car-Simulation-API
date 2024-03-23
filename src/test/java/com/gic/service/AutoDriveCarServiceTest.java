package com.gic.service;

import com.gic.models.*;
import com.gic.service.impl.AutoDriveCarServiceImpl;
import com.gic.utils.validationconstraints.RequestValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AutoDriveCarServiceTest {

    @InjectMocks
    private AutoDriveCarServiceImpl autoDriveCarService;

    @Mock
    private RequestValidator requestValidator;


    private static final AutonomousCar car1 = mock(AutonomousCar.class);
    private static final AutonomousCar car2 = mock(AutonomousCar.class);
    private static final AutonomousCar car3 = mock(AutonomousCar.class);
    private static final Dimension fieldDimension = mock(Dimension.class);
    private static final CarInputRequest carInputRequest_collision = mock(CarInputRequest.class);
    private static final CarInputRequest carInputRequest_noCollision = mock(CarInputRequest.class);

    @BeforeAll
    static void beforeSetup(){

        when(car1.getCommands()).thenReturn("FFRFFFFRRL");
        when(car1.getCurrentCoordinates()).thenReturn("1,2");
        when(car1.getCurrentFacingDirection()).thenReturn("N");
        when(car1.getName()).thenReturn("A");

        when(car2.getCommands()).thenReturn("FFLFFFFFFF");
        when(car2.getCurrentCoordinates()).thenReturn("7,8");
        when(car2.getCurrentFacingDirection()).thenReturn("W");
        when(car2.getName()).thenReturn("B");

        when(car3.getCommands()).thenReturn("FFRFFFRRLF");
        when(car3.getCurrentCoordinates()).thenReturn("6,3");
        when(car3.getCurrentFacingDirection()).thenReturn("E");
        when(car3.getName()).thenReturn("C");

        List<AutonomousCar> collisionCarList = new ArrayList<>();
        collisionCarList.add(car1);
        collisionCarList.add(car2);

        List<AutonomousCar> noCollisionCarList = new ArrayList<>();
        noCollisionCarList.add(car1);
        noCollisionCarList.add(car3);

        when(fieldDimension.getHeight()).thenReturn(10);
        when(fieldDimension.getWidth()).thenReturn(10);

        when(carInputRequest_collision.getFieldDimension()).thenReturn(fieldDimension);
        when(carInputRequest_collision.getCarInputDetailsList()).thenReturn(collisionCarList);

        when(carInputRequest_noCollision.getFieldDimension()).thenReturn(fieldDimension);
        when(carInputRequest_noCollision.getCarInputDetailsList()).thenReturn(noCollisionCarList);

    }

    @Test
    public void whenValidCarInputDetailsThenReturnCorrectEndingPosition() throws Exception {
        CarInputDetails carInputDetails = mock(CarInputDetails.class);
        when(carInputDetails.getFieldDimension()).thenReturn(fieldDimension);
        when(carInputDetails.getCommands()).thenReturn("FFRFFFRRLF");
        when(carInputDetails.getCurrentCoordinates()).thenReturn("1,2");
        when(carInputDetails.getCurrentFacingDirection()).thenReturn("N");

        CarEndingPosition carEndingPosition = autoDriveCarService.getCarEndingPositionAndDirection(carInputDetails);
        assertEquals('S',carEndingPosition.getDirection());
        assertEquals(4,carEndingPosition.getX());
        assertEquals(3,carEndingPosition.getY());

    }

    @Test
    public void whenMultipleCarsDeployOnSameFieldThenCheckCollision_Collision() throws Exception {
        CarCollisionResponse carCollisionResponse = mock(CarCollisionResponse.class);
        when(carCollisionResponse.getCarNames()).thenReturn("A B");
        when(carCollisionResponse.getCollisionPosition()).thenReturn("5 4");
        when(carCollisionResponse.getStep()).thenReturn(7);

        String collisionResult  = autoDriveCarService.isCarCollisionHappen(carInputRequest_collision);
        assertEquals(getCollisionResponse(carCollisionResponse),collisionResult);
    }

    @Test
    public void whenMultipleCarsDeployOnSameFieldThenCheckCollision_NoCollision() throws Exception {
        CarCollisionResponse carCollisionResponse = null;

        String collisionResult  = autoDriveCarService.isCarCollisionHappen(carInputRequest_noCollision);
        assertEquals(getCollisionResponse(carCollisionResponse),collisionResult);
    }

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
}
