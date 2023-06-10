package com.example.project.exceptions;

public class ClassRoomIsNotExistsException extends ClientFaultExceptions{
    public ClassRoomIsNotExistsException(String message) {
        super(message);
    }
}
