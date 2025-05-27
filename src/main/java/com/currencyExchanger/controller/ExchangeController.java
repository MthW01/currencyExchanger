package com.currencyExchanger.controller;

import com.currencyExchanger.service.ExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ExchangeController {
    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/exchangeRates")
    public ResponseEntity<?> getExchanges() {
        return new ResponseEntity<>(exchangeService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/exchangeRate/{code}")
    public ResponseEntity<?> getExchangeByCurrCodes(@PathVariable String code) {
        return new ResponseEntity<>(exchangeService.findByCode(code), HttpStatus.OK);
    }

    @PostMapping(value = "/exchangeRates",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createExchangeRate(@RequestParam Map<String, String> exchangeParams) {
        return new ResponseEntity<>(exchangeService.createExchange(exchangeParams), HttpStatus.OK);
    }

    @PatchMapping(value = "/exchangeRate/{code}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = "application/json")
    public ResponseEntity<?> updateExchangeRate(@RequestParam Map<String, String> params,
                                                @PathVariable String code) {
        return new ResponseEntity<>(exchangeService.updateExchangeRate(params, code), HttpStatus.OK);
    }

    @GetMapping(value = "/exchange")
    public ResponseEntity<?> getExchangeAmount(String from, String to, String amount) {
        return new ResponseEntity<>(exchangeService.getAmountForExchange(from, to, amount), HttpStatus.OK);
    }
}
