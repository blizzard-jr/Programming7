package org.example.exceptions;

public class NoSuchCommandException extends Exception{
    public NoSuchCommandException(String message){
        super(message);
    }
}
