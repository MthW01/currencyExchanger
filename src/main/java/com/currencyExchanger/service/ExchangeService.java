package com.currencyExchanger.service;

import com.currencyExchanger.dto.exchangeDto.ExchangeWithAmount;
import com.currencyExchanger.dto.exchangeDto.ExchangeWithObjectsDto;
import com.currencyExchanger.dto.exchangeDto.ExchangeWithoutIdDto;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.model.Exchange;
import com.currencyExchanger.repository.ExchangeRepository;
import com.currencyExchanger.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;

    public ExchangeWithObjectsDto createExchange(Map<String, String> exchangeParams) {
        var baseCurrCode = parseCode(exchangeParams.get(Utils.BASE_CURRENCY_CODE));
        var targetCurrCode = parseCode(exchangeParams.get(Utils.TARGET_CURRENCY_CODE));

        if (exchangeRepository.getExchangeByCode(baseCurrCode, targetCurrCode) == null) {
            var exchange = new ExchangeWithoutIdDto(
                    getCurrencyByCode(baseCurrCode).getId(),
                    getCurrencyByCode(targetCurrCode).getId(),
                    parseDouble(exchangeParams.get(Utils.RATE)));

            exchangeRepository.create(exchange);
            return createExchangeWithObjectsByIds(exchangeRepository.getExchangeByCode(baseCurrCode, targetCurrCode));
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exchange rate already exists");
        }
    }

    public ExchangeWithObjectsDto findByCode(String code) {
        var cuttedCode = cutCurrencyCodes(code);
        return Optional.ofNullable(exchangeRepository.getExchangeByCode(cuttedCode.get(0), cuttedCode.get(1)))
                .map(this::createExchangeWithObjectsByIds)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange rate not found"));
    }

    public List<ExchangeWithObjectsDto> findAll() {
        return exchangeRepository.getAll()
                .stream()
                .map(this::createExchangeWithObjectsByIds)
                .collect(Collectors.toList());
    }

    public ExchangeWithObjectsDto updateExchangeRate(Map<String, String> params, String code) {
        var rate = parseDouble(params.get(Utils.RATE));
        var cutCode = cutCurrencyCodes(code);
        return Optional.ofNullable(exchangeRepository.getExchangeByCode(cutCode.get(0), cutCode.get(1)))
                .map((Exchange exchange) -> createExchangeWithObjectsByIds(exchangeRepository.update(exchange.getId(), rate)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange rate not found"));
    }

    private ExchangeWithObjectsDto createExchangeWithObjectsByIds(Exchange exchange) {
        return new ExchangeWithObjectsDto(
                exchange.getId(),
                exchangeRepository.getCurrencyById(exchange.getBaseCurrencyId()),
                exchangeRepository.getCurrencyById(exchange.getTargetCurrencyId()),
                exchange.getRate());
    }

    private List<String> cutCurrencyCodes(String code) {
        try {
            return List.of(parseCode(code.substring(0, 3)), parseCode(code.substring(3)));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
    }

    public String parseCode(String code) {
        if (code.length() != 3 || !code.toUpperCase().matches("[A-Z]{3}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        return code.toUpperCase();
    }

    public Double parseDouble(String rate) {
        try {
            return Double.parseDouble(rate);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
    }

    public Currency getCurrencyByCode(String code) {
        return Optional.ofNullable(exchangeRepository.getCurrencyByCode(code))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found"));
    }

    public ExchangeWithAmount getAmountForExchange(String baseCurr, String targetCurr, String amountValue) {
        if (exchangeRepository.getCurrencyByCode(baseCurr) == null || exchangeRepository.getCurrencyByCode(targetCurr) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found");
        }
        double rate = getRate(baseCurr, targetCurr);
        double amount = parseDouble(amountValue);

        return new ExchangeWithAmount(
                exchangeRepository.getCurrencyByCode(baseCurr),
                exchangeRepository.getCurrencyByCode(targetCurr),
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
