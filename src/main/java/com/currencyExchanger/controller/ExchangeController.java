package com.currencyExchanger.controller;

import com.currencyExchanger.dto.exchangeDto.ExchangeWithObjectsDto;
import com.currencyExchanger.exceptionHandling.BadRequestException;
import com.currencyExchanger.exceptionHandling.ExceptionController;
import com.currencyExchanger.exceptionHandling.currenciesException.CurrencyNotFoundException;
import com.currencyExchanger.exceptionHandling.exchangeRatesExceptions.ExchangeRateByCodeNotFoundException;
import com.currencyExchanger.exceptionHandling.exchangeRatesExceptions.ExchangeRateIsAlreadyExistsException;
import com.currencyExchanger.exceptionHandling.exchangeRatesExceptions.ExchangeRatesNotFoundException;
import com.currencyExchanger.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ExchangeController {
    private final ExchangeService exchangeService;
    private final ExceptionController exceptionController = new ExceptionController();

    @Autowired
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/exchangeRates")
    public ResponseEntity<?> getExchanges() {
        var exchanges = exchangeService.findAll();
        return exchanges != null
                ? new ResponseEntity<>(exchanges, HttpStatus.OK)
                : exceptionController.getExceptionRequest(new ExchangeRatesNotFoundException());
    }

    @GetMapping("/exchangeRate/{code}")
    public ResponseEntity<?> getExchangeByCurrCodes(@PathVariable String code) {
        if (exchangeService.isIncorrectCode(code) || code.length() != 6) {
            return exceptionController.getExceptionRequest(new BadRequestException());
        }
        var exchange = exchangeService.findOne(code);
        return exchange != null
                ? new ResponseEntity<>(exchange, HttpStatus.OK)
                : exceptionController.getExceptionRequest(new ExchangeRateByCodeNotFoundException(code));
    }

    @PostMapping(value = "/exchangeRates",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = "application/json")
    public ResponseEntity<?> createCurrency(@RequestParam Map<String, String> exchangeParams) {
        var rate = exchangeService.tryParseRate(exchangeParams.get("rate"));
        var baseCurr = exchangeParams.get("baseCurrencyCode");
        var targetCurr = exchangeParams.get("targetCurrencyCode");
        if (exchangeService.isIncorrectCode(baseCurr) || exchangeService.isIncorrectCode(targetCurr) || rate == null) {
            return exceptionController.getExceptionRequest(new BadRequestException());
        } else if (exchangeService.getCurrencyByCode(baseCurr) == null || exchangeService.getCurrencyByCode(targetCurr) == null) {
            return exceptionController.getExceptionRequest(new CurrencyNotFoundException());
        }
        ExchangeWithObjectsDto newExchange = exchangeService.createExchange(baseCurr, targetCurr, rate);
        return newExchange != null
                ? new ResponseEntity<>(newExchange, HttpStatus.OK)
                : exceptionController.getExceptionRequest(new ExchangeRateIsAlreadyExistsException(baseCurr + targetCurr));
    }

    @PatchMapping(value = "/exchangeRate/{code}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = "application/json")
    public ResponseEntity<?> updateExchangeRate(@RequestParam Map<String, String> params, @PathVariable String code) {
        var rate = exchangeService.tryParseRate(params.get("rate"));
        if (exchangeService.isIncorrectCode(code) || code.length() != 6 || rate == null) {
            return exceptionController.getExceptionRequest(new BadRequestException());
        }
        var exchange = exchangeService.updateExchangeRate(rate, code);
        return exchange != null
                ? new ResponseEntity<>(exchange, HttpStatus.OK)
                : exceptionController.getExceptionRequest(new ExchangeRateByCodeNotFoundException(code));
    }

    @GetMapping(value = "/exchange")
    public ResponseEntity<?> getExchangeAmount(String from, String to, String amount) {
        if (exchangeService.isIncorrectCode(from) ||
                exchangeService.isIncorrectCode(to) ||
                exchangeService.tryParseRate(amount) == null) {
            return exceptionController.getExceptionRequest(new BadRequestException());
        }
        var exchange = exchangeService.getAmountForExchange(from, to, exchangeService.tryParseRate(amount));
        return exchange != null
                ? new ResponseEntity<>(exchange, HttpStatus.OK)
                : exceptionController.getExceptionRequest(new ExchangeRatesNotFoundException());
    }
}
