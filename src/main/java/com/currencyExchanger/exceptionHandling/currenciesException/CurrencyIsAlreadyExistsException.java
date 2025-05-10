package com.currencyExchanger.exceptionHandling.currenciesException;

import com.currencyExchanger.exceptionHandling.CommonException;
import org.springframework.http.HttpStatus;

public class CurrencyIsAlreadyExistsException extends CommonException {
    public CurrencyIsAlreadyExistsException(String code) {
        super("Валюта " + code + " уже существует", HttpStatus.CONFLICT);
    }
}