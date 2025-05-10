package com.currencyExchanger.exceptionHandling;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CommonException {
    public BadRequestException() {
        super("Некорректно введены данные", HttpStatus.BAD_REQUEST);
    }
}
