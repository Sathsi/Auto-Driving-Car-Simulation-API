package com.gic.exception;

import com.gic.utils.validationconstraints.ValidationConst;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarInputDetailValidationException extends Exception{

    private ValidationConst validationValue;

    public CarInputDetailValidationException(ValidationConst validationValue, final String message) {
        super(message);
        this.validationValue = validationValue;
    }
}
