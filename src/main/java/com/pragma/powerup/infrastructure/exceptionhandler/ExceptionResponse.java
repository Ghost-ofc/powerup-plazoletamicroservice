package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    INVALID_RESTAURANT_NAME("The restaurant name cannot consist of numbers only"),
    INVALID_NIT("The NIT must contain numeric characters only"),
    INVALID_PHONE("The restaurant phone number must contain up to 13 digits and may start with '+'"),
    USER_NOT_OWNER("The specified user does not exist or does not have the owner role"),
    RESTAURANT_ALREADY_EXISTS("A restaurant with this NIT already exists"),
    ACCESS_DENIED("Access denied: You do not have permissions for this action"),
    UNAUTHORIZED("Authentication failed or token invalid");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
