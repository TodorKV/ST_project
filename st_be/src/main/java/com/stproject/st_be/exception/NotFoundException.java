package com.stproject.st_be.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "No entity of type: %s, with ID: %s is found!";

    public NotFoundException(Class clazz, Integer id) {
        super(String.format(ERROR_MESSAGE, clazz.getSimpleName(), id));
    }
}
