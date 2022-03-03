package com.vahidsaghlatoon.homeservicesystem.controller.error;

import com.vahidsaghlatoon.homeservicesystem.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> handleException(Exception ex){
//        ErrorResponse error = ErrorResponse.builder()
//                .status(HttpStatus.BAD_REQUEST.value())
//                .message(ex.getMessage())
//                .time(Instant.now())
//                .build();
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    List<FieldErrorMessage> exceptionHandler(MethodArgumentNotValidException ex){
//        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//        return fieldErrors.stream().map(fieldError -> new FieldErrorMessage(fieldError.getField(),
//                fieldError.getDefaultMessage())).collect(Collectors.toList());
//    }


}
