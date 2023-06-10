package com.example.project.exceptions;

public class EmptyClassroomException extends ClientFaultExceptions{
    public EmptyClassroomException(String message) {
        super(message);
    }
}
