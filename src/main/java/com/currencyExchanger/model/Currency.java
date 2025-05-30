package com.currencyExchanger.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Currency {
    @Id
    private int id;
    private String name;
    private String code;
    private String sign;
}
