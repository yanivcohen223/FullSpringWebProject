package com.example.project.exceptions;

public class EmptyRequestException extends ClientFaultExceptions{
    public EmptyRequestException(String message) {
        super(message);
    }
}
