package com.currencyExchanger.service;

import com.currencyExchanger.dto.currencyDto.CurrencyWithoutIdDto;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Object createCurrency(CurrencyWithoutIdDto currency) {
        return currencyRepository.getByCode(currency.getCode()) == null
            ? currencyRepository.create(currency)
            : null;
    }

    public Currency getOne(String code) {
        return currencyRepository.getByCode(code.toUpperCase());
    }

    public List<Currency> getAll() {
        return currencyRepository.getAll();
    }

    public boolean isIncorrectCode(String code) {
        for (var codeChar: code.getBytes()) {
            if (codeChar > 'Z' || codeChar < 'A')
                return true;
        }
        return code.length() != 3;
    }
}
