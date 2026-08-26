package com.webdev2;

public class InvalidAgeException extends RuntimeException {

    public InvalidAgeException(int rejectedAge) {
        super("Invalid age supplied: " + rejectedAge + ". Age cannot be negative.");
    }

    public InvalidAgeException(String message) {
        super(message);
    }
}
