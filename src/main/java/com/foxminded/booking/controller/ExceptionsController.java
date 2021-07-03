package com.foxminded.booking.controller;

import com.foxminded.booking.model.dto.ErrorDto;
import com.foxminded.booking.model.exception.HttpStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionsController {
    @ExceptionHandler(HttpStatusException.class)
    public ResponseEntity<ErrorDto> handleHttpStatusException(HttpStatusException e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(e.getStatus());
        errorDto.setMessage(e.getMessage());
        return new ResponseEntity<>(errorDto, e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(HttpStatus.BAD_REQUEST);
        errorDto.setMessage("not valid due to validation error: " + e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatus(HttpStatus.CONFLICT);
        errorDto.setMessage("not valid due to constraint violation error: " + e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }
}
