package org.example.server.exceptions;

public class IllegalValueException extends RuntimeException{
    public IllegalValueException(String message){
        super(message);
    }
}
