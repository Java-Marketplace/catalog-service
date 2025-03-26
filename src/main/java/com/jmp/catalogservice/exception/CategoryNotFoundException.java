package com.jmp.catalogservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Category not found";

    public CategoryNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
