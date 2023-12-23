package com.sirma.academy.exception;

public class NegativeIdException extends EntityFactoryException{
    public NegativeIdException(String message){
        super(message);
    }
}
