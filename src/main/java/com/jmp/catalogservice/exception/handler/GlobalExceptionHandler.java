package com.jmp.catalogservice.exception.handler;

import com.jmp.catalogservice.dto.exception.ErrorResponse;
import com.jmp.catalogservice.exception.CategoryNotFoundException;
import com.jmp.catalogservice.exception.CircularDependencyException;
import com.jmp.catalogservice.exception.DuplicateCategoryException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return createGlobalErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateCategoryException(DuplicateCategoryException ex) {
        return createGlobalErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CircularDependencyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCircularDependencyException(CircularDependencyException ex) {
        return createGlobalErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    private ErrorResponse createGlobalErrorResponse(HttpStatus status, String message) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
    }
}
