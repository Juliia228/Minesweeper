package com.work.TestTask.exceptions;

// Ошибки при неверных входных данных post request
public class InvalidRequestException extends Exception {
    public InvalidRequestException(String message) {
        super(message);
    }
}
