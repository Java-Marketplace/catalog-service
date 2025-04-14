package com.jmp.catalogservice.exception;

public class DuplicateCategoryException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Duplicate category name detected: ";

    public DuplicateCategoryException(String categoryName) {
        super(ERROR_MESSAGE + categoryName);
    }
}
