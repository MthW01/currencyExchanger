package com.currencyExchanger.dto.exchangeDto;

import com.currencyExchanger.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeWithObjectsDto {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private Double rate;
}
