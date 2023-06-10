package com.example.project.exceptions;

public class NoAvailableClassesException extends ClientFaultExceptions{
    public NoAvailableClassesException(String message) {
        super(message);
    }
}
