package com.currencyExchanger.dto.exchangeDto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeWithoutIdDto {
    private int baseCurrencyId;
    private int targetCurrencyId;
    private Double rate;
}