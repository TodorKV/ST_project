package com.stproject.st_be.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Entity of type: %s, is not existing in DB!";

    public BadRequestException(Class clazz) {
        super(String.format(ERROR_MESSAGE, clazz.getSimpleName()));
    }
}
