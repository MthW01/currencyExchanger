package com.currencyExchanger.exceptionHandling.exchangeRatesExceptions;

import com.currencyExchanger.exceptionHandling.CommonException;
import org.springframework.http.HttpStatus;

public class ExchangeRatesNotFoundException extends CommonException {

    public ExchangeRatesNotFoundException() {
        super("Обменные курсы не найдены", HttpStatus.NOT_FOUND);
    }
}
