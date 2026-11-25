package com.corales.webdev2;

public class PetRequestValidationException extends RuntimeException {

    private final String field;

    public PetRequestValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
