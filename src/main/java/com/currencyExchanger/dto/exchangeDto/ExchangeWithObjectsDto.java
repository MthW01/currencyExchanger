package com.currencyExchanger.dto.exchangeDto;

import com.currencyExchanger.model.Currency;
import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeWithObjectsDto {
    @Id
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private Double rate;
}
