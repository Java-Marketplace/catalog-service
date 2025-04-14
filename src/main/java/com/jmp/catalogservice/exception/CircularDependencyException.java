package com.jmp.catalogservice.exception;

public class CircularDependencyException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Category cannot be a parent of itself";

    public CircularDependencyException() {
        super(ERROR_MESSAGE);
    }
}
