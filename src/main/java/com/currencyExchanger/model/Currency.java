package com.currencyExchanger.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Currency {
    @Id
    private int id;
    private String name;
    private String code;
    private String sign;
}
