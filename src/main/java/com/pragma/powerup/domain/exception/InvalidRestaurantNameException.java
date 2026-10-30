package com.pragma.powerup.domain.exception;

public class InvalidRestaurantNameException extends DomainException {
    public InvalidRestaurantNameException() {
        super("The restaurant name cannot consist of numbers only.");
    }
}
