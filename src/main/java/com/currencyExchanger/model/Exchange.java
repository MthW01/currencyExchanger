package com.currencyExchanger.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exchange {
    @Id
    private int id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private Double rate;
}
