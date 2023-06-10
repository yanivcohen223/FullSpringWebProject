package com.example.project.exceptions;

public class AllreadyExistsException extends ClientFaultExceptions{
    public AllreadyExistsException(String message) {
        super(message);
    }
}
