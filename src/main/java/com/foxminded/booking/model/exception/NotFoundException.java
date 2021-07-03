package com.foxminded.booking.model.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpStatusException {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(HttpStatus status) {
        super(status);
    }

    public NotFoundException(String message, HttpStatus status) {
        super(message, status);
    }

    public NotFoundException(String message, Throwable cause, HttpStatus status) {
        super(message, cause, status);
    }

    public NotFoundException(Throwable cause, HttpStatus status) {
        super(cause, status);
    }
}
