package com.currencyExchanger.controller;

import com.currencyExchanger.dto.currencyDto.CurrencyWithoutIdDto;
import com.currencyExchanger.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "localhost:63342")
@RestController
public class CurrencyController {
    private final CurrencyService service;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.service = currencyService;
    }

    @GetMapping("/currencies")
    public Object getCurrencies() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll());
    }

    @GetMapping("/currency/{code}")
    public ResponseEntity<?> getCurrencyByCode(@PathVariable String code) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getOne(code));
    }

    @PostMapping(value = "/currencies",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCurrency(@RequestParam Map<String, String> currencyParams) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.createCurrency(CurrencyWithoutIdDto.constructCurrency(currencyParams)));
    }
}
