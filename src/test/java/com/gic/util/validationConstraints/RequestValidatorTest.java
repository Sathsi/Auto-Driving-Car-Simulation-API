package com.gic.util.validationConstraints;

import com.gic.exception.CarInputDetailValidationException;
import com.gic.utils.validationconstraints.RequestValidator;
import com.gic.utils.validationconstraints.ValidationConst;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RequestValidatorTest {

    @InjectMocks
    private RequestValidator requestValidator;

    @Test
    public void whenInvalidStartCoordinatesThenThrowException() throws Exception {
        try {
            requestValidator.validateCarAutoDriveInputDetails("1,3","N","FFRLF");
        } catch (CarInputDetailValidationException e) {
            // Assert that the exception message is correct
            assertEquals(ValidationConst.INVALID_START_COORDINATES.message(), e.getMessage());
        }
    }

    @Test
    public void whenInvalidStartDirectionThenThrowException() throws Exception {
        try {
            requestValidator.validateCarAutoDriveInputDetails("1,2","T","FFRLF");
        } catch (CarInputDetailValidationException e) {
            // Assert that the exception message is correct
            assertEquals(ValidationConst.INVALID_DIRECTION.message(), e.getMessage());
        }
    }

    @Test
    public void whenInvalidCommandThenThrowException() throws Exception {
        try {
            requestValidator.validateCarAutoDriveInputDetails("1,2","S","FFRTF");
        } catch (CarInputDetailValidationException e) {
            // Assert that the exception message is correct
            assertEquals(ValidationConst.INVALID_COMMAND.message(), e.getMessage());
        }
    }
}
