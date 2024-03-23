package com.gic.utils.validationconstraints;

public enum ValidationConst {

    INVALID_START_COORDINATES("Invalid car starting coordinates"),
    INSUFFICIENT_COORDINATES("Insufficient coordinates"),
    INVALID_DIRECTION("Invalid direction"),
    INVALID_COMMAND("Invalid Command"),
    NO_MULTIPLE_CARS("Enter multiple car details"),
    INVALID_COORDINATES("Invalid start coordinates. Coordinates can not be null or empty"),
    NULL_DIRECTION("Start direction can not be null or empty"),
    NULL_COMMAND("Command can not be null or empty");

    private final String msg;

    ValidationConst(String msg) {
        this.msg = msg;
    }

    public String message() {
        return msg;
    }
}
