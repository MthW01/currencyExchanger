package com.currencyExchanger.dto.exchangeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeWithoutIdDto {
    private int baseCurrencyId;
    private int targetCurrencyId;
    private double rate;
}