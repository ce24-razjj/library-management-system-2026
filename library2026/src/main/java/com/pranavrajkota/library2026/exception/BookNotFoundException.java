package com.pranavrajkota.library2026.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message){
        super(message);
    }

}