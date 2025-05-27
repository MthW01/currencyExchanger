package com.currencyExchanger.dto.exchangeDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeWithoutIdDto {
    private int baseCurrencyId;
    private int targetCurrencyId;
    private Double rate;
}