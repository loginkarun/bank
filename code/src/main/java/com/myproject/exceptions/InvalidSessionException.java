package com.myproject.exceptions;

public class InvalidSessionException extends RuntimeException {
    
    public InvalidSessionException(String message) {
        super(message);
    }
    
    public InvalidSessionException() {
        super("Invalid user session");
    }
}
