package com.currencyExchanger.controller;

import com.currencyExchanger.dto.exchangeDto.ExchangeWithAmount;
import com.currencyExchanger.dto.exchangeDto.ExchangeWithObjectsDto;
import com.currencyExchanger.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ExchangeController {
    private final ExchangeService exchangeService;

    @GetMapping("/exchangeRates")
    public List<ExchangeWithObjectsDto> getExchanges() {
        return exchangeService.findAll();
    }

    @GetMapping("/exchangeRate/{code}")
    public ExchangeWithObjectsDto getExchangeByCurrCodes(@PathVariable String code) {
        return exchangeService.findByCode(code);
    }

    @PostMapping(value = "/exchangeRates",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ExchangeWithObjectsDto createExchangeRate(@RequestParam Map<String, String> exchangeParams) {
        return exchangeService.createExchange(exchangeParams);
    }

    @PatchMapping(value = "/exchangeRate/{code}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ExchangeWithObjectsDto updateExchangeRate(@RequestParam Map<String, String> params,
                                                     @PathVariable String code) {
        return exchangeService.updateExchangeRate(params, code);
    }

    @GetMapping(value = "/exchange")
    public ExchangeWithAmount getExchangeAmount(String from, String to, String amount) {
        return exchangeService.getAmountForExchange(from, to, amount);
    }
}
