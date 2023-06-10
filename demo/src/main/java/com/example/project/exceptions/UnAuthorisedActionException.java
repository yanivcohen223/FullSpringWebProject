package com.example.project.exceptions;

public class UnAuthorisedActionException extends ClientFaultExceptions{
    public UnAuthorisedActionException(String message) {
        super(message);
    }
}
