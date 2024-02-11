package com.work.TestTask.controllers;

import com.work.TestTask.classes.MyError;
import com.work.TestTask.exceptions.ForbiddenMoveException;
import com.work.TestTask.exceptions.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// обработчик ошибок, отправляет данные об ошибках в http response
@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(ForbiddenMoveException.class)
    public ResponseEntity<MyError> handleException(ForbiddenMoveException moveEx) {
        MyError error = new MyError(moveEx);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<MyError> handleException(InvalidRequestException invalidReqEx) {
        MyError error = new MyError(invalidReqEx);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyError> handleException(Exception anyEx) {
        MyError error = new MyError(anyEx);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
