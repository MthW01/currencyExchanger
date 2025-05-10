package com.currencyExchanger.service;

import com.currencyExchanger.dto.exchangeDto.ExchangeWithAmount;
import com.currencyExchanger.dto.exchangeDto.ExchangeWithObjectsDto;
import com.currencyExchanger.dto.exchangeDto.ExchangeWithoutIdDto;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.model.Exchange;
import com.currencyExchanger.repository.ExchangeRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;

    @Autowired
    public ExchangeService(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    public ExchangeWithObjectsDto createExchange(String baseCurrCode, String targetCurrCode, double rate) {
        var baseCurr = exchangeRepository.getCurrencyByCode(baseCurrCode);
        var targetCurr = exchangeRepository.getCurrencyByCode(targetCurrCode);
        if (exchangeRepository.getExchangeByCode(baseCurrCode, targetCurrCode) == null) {
            var exchange = new ExchangeWithoutIdDto(
                    baseCurr.getId(),
                    targetCurr.getId(),
                    rate);

            return createExchangeWithObjectsByIds(exchangeRepository.create(exchange));
        } else return null;
    }

    public ExchangeWithAmount getAmountForExchange(String baseCurr, String targetCurr, double amount) {

        double rate = getRate(baseCurr, targetCurr);

        return new ExchangeWithAmount(
                exchangeRepository.getCurrencyByCode(baseCurr),
                exchangeRepository.getCurrencyByCode(targetCurr),
                rate,
                amount,
                amount * rate);
    }

    private double getRate(String baseCurr, String targetCurr) {
        if (exchangeRepository.getExchangeByCode(baseCurr, targetCurr) == null & exchangeRepository.getExchangeByCode(targetCurr, baseCurr) == null) {
            var rates = exchangeRepository.getRatesByUSD(baseCurr, targetCurr);
            return rates.get(0) / rates.get(1);
        } else if (exchangeRepository.getExchangeByCode(baseCurr, targetCurr) == null) {
            return 1 / exchangeRepository.getExchangeByCode(targetCurr, baseCurr).getRate();
        } else {
            return exchangeRepository.getExchangeByCode(baseCurr, targetCurr).getRate();
        }
    }

    public ExchangeWithObjectsDto findOne(String code) {
        var cuttedCode = cutCurrencyCodes(code);
        Exchange exchange = exchangeRepository.getExchangeByCode(cuttedCode.get(0), cuttedCode.get(1));
        if (exchange == null) return null;
        else return createExchangeWithObjectsByIds(exchange);
    }

    public List<ExchangeWithObjectsDto> findAll() {
        var listOfExchangesWithObjects = new ArrayList<ExchangeWithObjectsDto>();
        for (Exchange exchange : exchangeRepository.getAll()) {
            listOfExchangesWithObjects.add(createExchangeWithObjectsByIds(exchange));
        }
        return listOfExchangesWithObjects;
    }

    public ExchangeWithObjectsDto updateExchangeRate(double rate, String code) {
        var cuttedCode = cutCurrencyCodes(code);
        Exchange exchange = exchangeRepository.getExchangeByCode(cuttedCode.get(0), cuttedCode.get(1));
        if (exchange == null) return null;
        else return createExchangeWithObjectsByIds(exchangeRepository.update(exchange.getId(), rate));
    }

    private ExchangeWithObjectsDto createExchangeWithObjectsByIds(Exchange exchange) {
        return new ExchangeWithObjectsDto(
                exchange.getId(),
                exchangeRepository.getCurrencyById(exchange.getBaseCurrencyId()),
                exchangeRepository.getCurrencyById(exchange.getTargetCurrencyId()),
                exchange.getRate());
    }

    private ArrayList<String> cutCurrencyCodes(String code) {
        var array = new ArrayList<String>();
        array.add(code.substring(0, 3));
        array.add(code.substring(3));
        return array;
    }

    public boolean isIncorrectCode(String code) {
        for (var codeChar : code.getBytes()) {
            if (codeChar > 'Z' || codeChar < 'A')
                return true;
        }
        return false;
    }

    public Double tryParseRate(String rate) {
        try {
            return Double.parseDouble(rate);
        } catch (Exception e) {
            return null;
        }
    }

    public Currency getCurrencyByCode(String code) {
        return exchangeRepository.getCurrencyByCode(code);
    }
}
