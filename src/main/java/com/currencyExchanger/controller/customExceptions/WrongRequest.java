package com.currencyExchanger.controller.customExceptions;

public class WrongRequest extends RuntimeException {
    public WrongRequest(String message) {
        super(message + " is entered incorrectly");
    }
}
