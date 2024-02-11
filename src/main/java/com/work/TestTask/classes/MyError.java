package com.work.TestTask.classes;

import lombok.Data;

// класс, предназначенный для отправления http response в случае ошибки с описанием ошибки
@Data
public class MyError {
    private String error;

    public MyError(Exception exception) {
        error = exception.getMessage();
    }
}
