package com.currencyExchanger.exceptionHandling;

import org.springframework.http.HttpStatus;

public abstract class CommonException {
    public String MESSAGE;
    public HttpStatus HTTP_STATUS;

    public CommonException(String message, HttpStatus httpStatus) {
        this.MESSAGE = message;
        this.HTTP_STATUS = httpStatus;
    }
}
