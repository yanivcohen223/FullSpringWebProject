package com.example.project.exceptions;

public class ClassroomTypeIsNotExistsException extends ClientFaultExceptions{
    public ClassroomTypeIsNotExistsException(String message) {
        super(message);
    }
}
