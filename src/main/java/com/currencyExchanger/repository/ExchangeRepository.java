package com.currencyExchanger.repository;

import com.currencyExchanger.dto.exchangeDto.ExchangeWithoutIdDto;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.model.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ExchangeRepository {
    private final JdbcTemplate template;
    private static final AtomicInteger EXCHANGE_ID = new AtomicInteger();

    @Autowired
    ExchangeRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public List<Exchange> getAll() {
        return template.query("SELECT id, basecurrencyid, targetcurrencyid, rate " +
                        "FROM exchangerates",
                new BeanPropertyRowMapper<>(Exchange.class));
    }

    public Exchange getExchangeByCode(String baseCode, String targetCode) {
        return template.query("SELECT exch.id, exch.basecurrencyid, exch.targetcurrencyid, exch.rate " +
                                "FROM exchangerates exch " +
                                "JOIN currencies basecur ON exch.basecurrencyid = basecur.id " +
                                "JOIN currencies targetcur ON exch.targetcurrencyid = targetcur.id " +
                                "WHERE basecur.code = ? AND targetcur.code = ?",
                        new BeanPropertyRowMapper<>(Exchange.class), baseCode, targetCode)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Exchange getExchangeById(int id) {
        return template.query("SELECT id, basecurrencyid, targetcurrencyid, rate " +
                                "FROM exchangerates " +
                                "WHERE id = ?",
                        new BeanPropertyRowMapper<>(Exchange.class), id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Exchange update(int id, Double rate) {
        template.update("UPDATE exchangerates " +
                "SET rate = ? " +
                "WHERE id = ?", rate, id);

        return getExchangeById(id);
    }

    public Exchange create(ExchangeWithoutIdDto exchange) {
        final int id = EXCHANGE_ID.incrementAndGet();
        template.update("INSERT INTO exchangerates (id, basecurrencyid, targetcurrencyid, rate) VALUES (?,?,?,?)",
                id,
                exchange.getBaseCurrencyId(),
                exchange.getTargetCurrencyId(),
                exchange.getRate());
        return getExchangeById(id);
    }

    public Currency getCurrencyById(int id) {
        return template.query("SELECT id, code, sign, name FROM currencies WHERE id = ?",
                        new BeanPropertyRowMapper<>(Currency.class), id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Currency getCurrencyByCode(String code) {
        return template.query("SELECT id, code, sign, name FROM currencies WHERE code = ?",
                        new BeanPropertyRowMapper<>(Currency.class), code)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<Double> getRatesByUSD(String baseCurr, String targetCurr) {
        var request = template.query("SELECT rate FROM exchangerates " +
                    "WHERE basecurrencyid = ? AND (targetcurrencyid = ? OR targetcurrencyid = ?)",
                    (rs, rowNum) -> rs.getDouble(1),
                    getCurrencyByCode("USD").getId(),
                    getCurrencyByCode(baseCurr).getId(),
                    getCurrencyByCode(targetCurr).getId());
        return request.size() != 2 ? null : request;
    }
}
