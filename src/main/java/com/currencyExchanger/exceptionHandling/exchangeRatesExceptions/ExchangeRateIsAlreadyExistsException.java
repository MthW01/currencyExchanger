package com.currencyExchanger.exceptionHandling.exchangeRatesExceptions;

import com.currencyExchanger.exceptionHandling.CommonException;
import org.springframework.http.HttpStatus;

public class ExchangeRateIsAlreadyExistsException extends CommonException {

    public ExchangeRateIsAlreadyExistsException(String code) {
        super("Обменные курс " + code + " уже существует", HttpStatus.CONFLICT);
    }
}
