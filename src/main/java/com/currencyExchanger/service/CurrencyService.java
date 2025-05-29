package com.currencyExchanger.service;

import com.currencyExchanger.controller.customExceptions.WrongRequest;
import com.currencyExchanger.dto.currencyDto.CurrencyWithoutIdDto;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.repository.CurrencyRepository;
import com.currencyExchanger.utils.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public Currency createCurrency(CurrencyWithoutIdDto currency) {
        if (currencyRepository.getByCode(currency.getCode()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Currency is already exists");
        } else {
            if (!Validator.isCodeValid(currency.getCode())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
            }
            return currencyRepository.create(currency);
        }
    }

    public Currency getOne(String code) {
        if (!Validator.isCodeValid(code)) {
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
            throw new WrongRequest("Currency value");
        }
        if (currencyRepository.getByCode(code.toUpperCase()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found");
        } else {
            return currencyRepository.getByCode(code.toUpperCase());
        }
    }

    public List<Currency> getAll() {
        return currencyRepository.getAll();
    }

}
