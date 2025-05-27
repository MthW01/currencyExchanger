package com.currencyExchanger.dto.currencyDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class CurrencyWithoutIdDto {
    private String name;
    private String code;
    private String sign;

    public static CurrencyWithoutIdDto constructCurrency(Map<String, String> currParams) {
        if (currParams.get("name") == null || currParams.get("code") == null || currParams.get("sign") == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        return new CurrencyWithoutIdDto(
                currParams.get("name"),
                currParams.get("code"),
                currParams.get("sign")
        );
    }
}
