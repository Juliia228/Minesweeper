package com.work.TestTask.exceptions;

// Ошибки при невозможности совершить ход
public class ForbiddenMoveException extends Exception {
    public ForbiddenMoveException(String message) {
        super(message);
    }
}
