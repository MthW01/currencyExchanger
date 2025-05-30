package com.currencyExchanger.controller;

import com.currencyExchanger.dto.currencyDto.CurrencyWithoutIdDto;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CurrencyController {
    private final CurrencyService service;

    @GetMapping("/currencies")
    public List<Currency> getCurrencies() {
        return service.getAll();
    }

    @GetMapping("/currency/{code}")
    public Currency getCurrencyByCode(@PathVariable String code) {
        return service.getOne(code);
    }

    @PostMapping(value = "/currencies",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Currency createCurrency(@RequestParam Map<String, String> currencyParams) {
        return service.createCurrency(CurrencyWithoutIdDto.constructCurrency(currencyParams));
    }
}
