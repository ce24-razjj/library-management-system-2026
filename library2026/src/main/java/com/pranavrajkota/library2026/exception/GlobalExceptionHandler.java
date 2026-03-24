//package com.pranavrajkota.library2026.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(BookNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex){
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.NOT_FOUND.value(),
//                ex.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
////    @ExceptionHandler(MethodArgumentNotValidException.class)
////    public ResponseEntity<Map<String,String>> handleValidation(MethodArgumentNotValidException ex){
////
////        Map<String,String> errors = new HashMap<>();
////
////        ex.getBindingResult().getFieldErrors().forEach(error ->
////                errors.put(error.getField(), error.getDefaultMessage())
////        );
////
////        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
////    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
//
//        String message = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .findFirst()
//                .map(error -> error.getField() + " : " + error.getDefaultMessage())
//                .orElse("Validation failed");
//
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                message,
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex){
//
//        ErrorResponse error = new ErrorResponse(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                ex.getMessage(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}































package com.pranavrajkota.library2026.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Book not found
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Book not available
    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleBookNotAvailable(BookNotAvailableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .orElse("Validation failed");

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    //  Catch-all (DO NOT expose internal errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
    }

    // Common response builder (clean + reusable)
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {

        ErrorResponse error = new ErrorResponse(
                status.value(),
                message,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, status);
    }
}