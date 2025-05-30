package com.currencyExchanger.service;

import com.currencyExchanger.controller.customExceptions.AlreadyExistsException;
import com.currencyExchanger.controller.customExceptions.NotFoundException;
import com.currencyExchanger.controller.customExceptions.WrongRequestException;
import com.currencyExchanger.dto.exchangeDto.ExchangeWithAmount;
import com.currencyExchanger.dto.exchangeDto.ExchangeWithObjectsDto;
import com.currencyExchanger.dto.exchangeDto.ExchangeWithoutIdDto;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.model.Exchange;
import com.currencyExchanger.repository.ExchangeRepository;
import com.currencyExchanger.utils.Utils;
import com.currencyExchanger.utils.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;

    public ExchangeWithObjectsDto createExchange(Map<String, String> exchangeParams) {
        var baseCurrCode = Validator.parseCode(exchangeParams.get(Utils.BASE_CURRENCY_CODE));
        var targetCurrCode = Validator.parseCode(exchangeParams.get(Utils.TARGET_CURRENCY_CODE));

        if (exchangeRepository.getExchangeByCode(baseCurrCode, targetCurrCode) == null) {
            var exchange = new ExchangeWithoutIdDto(
                    getNotNullCurrencyByCode(baseCurrCode).getId(),
                    getNotNullCurrencyByCode(targetCurrCode).getId(),
                    Validator.parseDouble(exchangeParams.get(Utils.RATE)));

            exchangeRepository.create(exchange);
            return createExchangeWithObjectsByIds(getNotNullExchangeByCode(baseCurrCode, targetCurrCode));
        } else {
            throw new AlreadyExistsException("Exchange rate " + baseCurrCode + targetCurrCode);
        }
    }

    public ExchangeWithObjectsDto findByCode(String code) {
        return createExchangeWithObjectsByIds(getNotNullExchangeByCode(getFirstCode(code), getSecondCode(code)));
    }

    public List<ExchangeWithObjectsDto> findAll() {
        return exchangeRepository.getAll()
                .stream()
                .map(this::createExchangeWithObjectsByIds)
                .collect(Collectors.toList());
    }

    public ExchangeWithObjectsDto updateExchangeRate(Map<String, String> params, String code) {
        var rate = Validator.parseDouble(params.get(Utils.RATE));
        return Optional.ofNullable(exchangeRepository.getExchangeByCode(getFirstCode(code), getSecondCode(code)))
                .map((Exchange exchange) -> createExchangeWithObjectsByIds(exchangeRepository.update(exchange.getId(), rate)))
                .orElseThrow(() -> new NotFoundException("Exchange rate " + code));
    }

    private ExchangeWithObjectsDto createExchangeWithObjectsByIds(Exchange exchange) {
        return new ExchangeWithObjectsDto(
                exchange.getId(),
                exchangeRepository.getCurrencyById(exchange.getBaseCurrencyId()),
                exchangeRepository.getCurrencyById(exchange.getTargetCurrencyId()),
                exchange.getRate());
    }

    private String getSecondCode(String code) {
        try {
            return Validator.parseCode(code.substring(3));
        } catch (Exception e) {
            throw new WrongRequestException("Currency value " + code);
        }
    }

    private String getFirstCode(String code) {
        try {
            return Validator.parseCode(code.substring(0, 3));
        } catch (Exception e) {
            throw new WrongRequestException("Currency value " + code);
        }
    }

    public Currency getNotNullCurrencyByCode(String code) {
        if (exchangeRepository.getCurrencyByCode(code) == null) {
            throw new NotFoundException("Currency " + code);
        } else {
            return exchangeRepository.getCurrencyByCode(code);
        }
    }

    public Exchange getNotNullExchangeByCode(String baseCode, String targetCode) {
        if (exchangeRepository.getExchangeByCode(baseCode, targetCode) == null) {
            throw new NotFoundException("Exchange rate " + baseCode + targetCode);
        } else {
            return exchangeRepository.getExchangeByCode(baseCode, targetCode);
        }
    }

    public ExchangeWithAmount getAmountForExchange(String baseCurr, String targetCurr, String amountValue) {
        double rate = getRate(baseCurr, targetCurr);
        double amount = Validator.parseDouble(amountValue);

        return new ExchangeWithAmount(
                getNotNullCurrencyByCode(baseCurr),
                getNotNullCurrencyByCode(targetCurr),
                rate,
                amount,
                amount * rate);
    }

    private double getRate(String baseCurr, String targetCurr) {
        if (exchangeRepository.getExchangeByCode(baseCurr, targetCurr) == null && exchangeRepository.getExchangeByCode(targetCurr, baseCurr) == null) {
            var rates = exchangeRepository.getRatesByUSD(baseCurr, targetCurr);
            return rates.get(0) / rates.get(1);
        } else if (exchangeRepository.getExchangeByCode(baseCurr, targetCurr) == null) {
            return 1 / exchangeRepository.getExchangeByCode(targetCurr, baseCurr).getRate();
        } else {
            return exchangeRepository.getExchangeByCode(baseCurr, targetCurr).getRate();
        }
    }
}
