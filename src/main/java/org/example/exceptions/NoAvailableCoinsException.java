package org.example.exceptions;

public class NoAvailableCoinsException extends RuntimeException{
    public NoAvailableCoinsException(String message){
        super(message);
    }
}
