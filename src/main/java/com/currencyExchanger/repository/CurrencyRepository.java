package com.currencyExchanger.repository;

import com.currencyExchanger.dto.currencyDto.CurrencyWithoutIdDto;
import com.currencyExchanger.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CurrencyRepository {

    private final JdbcTemplate template;
    private static final AtomicInteger CURRENCY_ID = new AtomicInteger();

    @Autowired
    CurrencyRepository(DataSource dataSource) {
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
        final int id = CURRENCY_ID.incrementAndGet();
        template.update("INSERT INTO currencies (id, code, sign, name) VALUES (?,?,?,?)",
                id,
                currency.getCode(),
                currency.getSign(),
                currency.getName());
        return new Currency(id, currency.getCode(), currency.getSign(), currency.getName());
    }
}
