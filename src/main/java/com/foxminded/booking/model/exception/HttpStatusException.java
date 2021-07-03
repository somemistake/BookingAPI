package com.foxminded.booking.model.exception;

import org.springframework.http.HttpStatus;

public class HttpStatusException extends RuntimeException {
    private HttpStatus status;

    public HttpStatusException() {
    }

    public HttpStatusException(String message) {
        super(message);
    }

    public HttpStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatusException(Throwable cause) {
        super(cause);
    }

    public HttpStatusException(HttpStatus status) {
        this.status = status;
    }

    public HttpStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatusException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatusException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
