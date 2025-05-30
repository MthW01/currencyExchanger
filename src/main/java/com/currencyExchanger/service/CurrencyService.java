package com.currencyExchanger.service;

import com.currencyExchanger.controller.customExceptions.AlreadyExistsException;
import com.currencyExchanger.controller.customExceptions.NotFoundException;
import com.currencyExchanger.controller.customExceptions.WrongRequestException;
import com.currencyExchanger.dto.currencyDto.CurrencyWithoutIdDto;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.repository.CurrencyRepository;
import com.currencyExchanger.utils.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public Currency createCurrency(CurrencyWithoutIdDto currency) {
        if (!Validator.isCodeValid(currency.getCode())) {
            throw new WrongRequestException("Currency value");
        } else {
            if (currencyRepository.getByCode(currency.getCode().toUpperCase()) != null) {
                throw new AlreadyExistsException("Currency " + currency.getCode());
            } else {
                return currencyRepository.create(currency);
            }
        }
    }

    public Currency getOne(String code) {
        if (!Validator.isCodeValid(code)) {
            throw new WrongRequestException("Currency value");
        } else {
            if (currencyRepository.getByCode(code.toUpperCase()) == null) {
                throw new NotFoundException("Currency" + code);
            } else {
                return currencyRepository.getByCode(code);
            }
        }
    }

    public List<Currency> getAll() {
        return currencyRepository.getAll();
    }

}
