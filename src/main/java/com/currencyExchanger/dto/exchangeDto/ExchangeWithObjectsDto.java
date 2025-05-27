package com.currencyExchanger.dto.exchangeDto;

import com.currencyExchanger.model.Currency;
import lombok.*;

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
