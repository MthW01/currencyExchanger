package com.currencyExchanger.exceptionHandling.currenciesException;

import com.currencyExchanger.exceptionHandling.CommonException;
import org.springframework.http.HttpStatus;

public class CurrencyNotFoundException extends CommonException {
    public CurrencyNotFoundException() {
        super("Валюта не найдена", HttpStatus.NOT_FOUND);
    }
}
