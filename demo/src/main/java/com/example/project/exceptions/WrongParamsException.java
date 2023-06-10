package com.example.project.exceptions;

public class WrongParamsException extends ClientFaultExceptions{
    public WrongParamsException(String message) {
        super(message);
    }
}
