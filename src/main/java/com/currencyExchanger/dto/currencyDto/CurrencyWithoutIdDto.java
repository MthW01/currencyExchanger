package com.currencyExchanger.dto.currencyDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CurrencyWithoutIdDto {
    private String name;
    private String code;
    private String sign;
}
