package com.currencyExchanger.controller;

import com.currencyExchanger.dto.currencyDto.CurrencyWithoutIdDto;
import com.currencyExchanger.exceptionHandling.BadRequestException;
import com.currencyExchanger.exceptionHandling.currenciesException.CurrencyIsAlreadyExistsException;
import com.currencyExchanger.exceptionHandling.currenciesException.CurrencyNotFoundException;
import com.currencyExchanger.exceptionHandling.ExceptionController;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CurrencyController {
    private final CurrencyService service;
    private ExceptionController exceptionHandler = new ExceptionController();

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.service = currencyService;
    }

    @GetMapping("/currencies")
    public Object getCurrencies() {
        List<Currency> currencies = service.getAll();
        return currencies != null
                ? new ResponseEntity<>(currencies, HttpStatus.OK)
                : exceptionHandler.getExceptionRequest(new CurrencyNotFoundException());
    }

    @GetMapping("/currency/{code}")
    public ResponseEntity<?> getCurrencyByCode(@PathVariable String code) {
        if (service.isIncorrectCode(code.toUpperCase())) {
            return exceptionHandler.getExceptionRequest(new BadRequestException());
        }
        Currency currency = service.getOne(code);
        return currency != null
                ? new ResponseEntity<>(currency, HttpStatus.OK)
                : exceptionHandler.getExceptionRequest(new CurrencyNotFoundException());
    }

    @PostMapping(value = "/currencies",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = "application/json")
    public ResponseEntity<?> createCurrency(@RequestParam Map<String, String> currencyParams) {
        if (service.isIncorrectCode(currencyParams.get("code").toUpperCase())) {
            return exceptionHandler.getExceptionRequest(new BadRequestException());
        } else {
            CurrencyWithoutIdDto currency = new CurrencyWithoutIdDto();
            currency.setCode(currencyParams.get("code").toUpperCase());
            currency.setSign(currencyParams.get("sign"));
            currency.setName(currencyParams.get("name"));
            var newCurrency = service.createCurrency(currency);
            return newCurrency != null
                    ? new ResponseEntity<>(newCurrency, HttpStatus.OK)
                    : exceptionHandler.getExceptionRequest(new CurrencyIsAlreadyExistsException(currency.getCode()));
        }
    }
}
