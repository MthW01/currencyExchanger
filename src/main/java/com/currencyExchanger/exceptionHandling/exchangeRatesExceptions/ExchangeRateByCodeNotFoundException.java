package com.currencyExchanger.exceptionHandling.exchangeRatesExceptions;

import com.currencyExchanger.exceptionHandling.CommonException;
import org.springframework.http.HttpStatus;

public class ExchangeRateByCodeNotFoundException extends CommonException {

    public ExchangeRateByCodeNotFoundException(String code) {
        super("Обменный курс " + code + " не найден", HttpStatus.NOT_FOUND);
    }
}
