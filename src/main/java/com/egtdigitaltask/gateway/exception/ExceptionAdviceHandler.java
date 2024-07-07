package com.egtdigitaltask.gateway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionAdviceHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdviceHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> exceptionIllegalArgument(IllegalArgumentException exception)
    {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> exceptionMethodArgumentNotValid(
            MethodArgumentNotValidException exception)
    {


        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) ->
                                                     {
                                                         String fieldName = ((FieldError) error).getField();
                                                         String errorMessage = error.getDefaultMessage();
                                                         errors.put(fieldName, errorMessage);
                                                         LOGGER.error("Validation failed for field " + fieldName, error);
                                                     });
        return ResponseEntity.badRequest().body(errors);
    }
}
