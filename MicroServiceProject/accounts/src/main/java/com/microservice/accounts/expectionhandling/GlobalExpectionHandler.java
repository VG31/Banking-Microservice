package com.microservice.accounts.expectionhandling;

import com.microservice.accounts.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExpectionHandler {

    @ExceptionHandler(CustomerAlreadyPresentException.class)
    public ResponseEntity<ErrorDto> handleCustomerAlreadyPresentException(CustomerAlreadyPresentException exception, WebRequest webRequest) {

        ErrorDto errorDto = new ErrorDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorDto> handleResourceNotFound(ResourceNotFound exception, WebRequest webRequest) {

        ErrorDto errorDto = new ErrorDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }
}
