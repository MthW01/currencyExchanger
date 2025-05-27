package com.currencyExchanger.service;

import com.currencyExchanger.dto.currencyDto.CurrencyWithoutIdDto;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Object createCurrency(CurrencyWithoutIdDto currency) {
        if (currencyRepository.getByCode(currency.getCode()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Currency is already exists");
        } else {
            if (isIncorrectCode(currency.getCode())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
            }
            return currencyRepository.create(currency);
        }
    }

    public Currency getOne(String code) {
        if (isIncorrectCode(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
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

    public static boolean isIncorrectCode(String code) {
        if (code.length() == 3) {
            for (var codeChar : code.toUpperCase().getBytes()) {
                if (codeChar > 'Z' || codeChar < 'A')
                    return true;
            }
            return false;
        } else {
            return true;
        }
    }
}
