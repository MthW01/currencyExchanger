package com.currencyExchanger.dto.errorDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Data
public class CurrencyErrorDto {
    private String message;
}
