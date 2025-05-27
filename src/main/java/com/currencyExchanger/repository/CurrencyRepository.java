package com.currencyExchanger.repository;

import com.currencyExchanger.dto.currencyDto.CurrencyWithoutIdDto;
import com.currencyExchanger.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CurrencyRepository {

    private final JdbcTemplate template;

    @Autowired
    public CurrencyRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public List<Currency> getAll() {
        return template.query("SELECT id, code, sign, name FROM currencies", new BeanPropertyRowMapper<>(Currency.class));
    }

    public Currency getByCode(String code) {
        return template.query("SELECT id, code, sign, name FROM currencies WHERE code = ?",
                        new BeanPropertyRowMapper<>(Currency.class), code)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Currency create(CurrencyWithoutIdDto currency) {
        template.update("INSERT INTO currencies (code, sign, name) VALUES (?,?,?)",
                currency.getCode(),
                currency.getSign(),
                currency.getName());
        return getByCode(currency.getCode());
    }
}
