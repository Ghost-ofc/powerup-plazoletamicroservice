package com.pragma.powerup.domain.exception;

public class InvalidNitException extends DomainException {
    public InvalidNitException() {
        super("The NIT must contain numeric characters only.");
    }
}
