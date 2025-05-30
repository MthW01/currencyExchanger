package com.currencyExchanger.controller.customExceptions;

public class WrongRequestException extends RuntimeException {
    public WrongRequestException(String message) {
        super(message + " is entered incorrectly");
    }
}
