package com.currencyExchanger.repository;

import com.currencyExchanger.dto.exchangeDto.ExchangeWithoutIdDto;
import com.currencyExchanger.model.Currency;
import com.currencyExchanger.model.Exchange;
import com.currencyExchanger.utils.Utils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ExchangeRepository {
    private final JdbcTemplate template;

    public ExchangeRepository(DataSource dataSource) {
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

    public void create(ExchangeWithoutIdDto exchange) {
        template.update("INSERT INTO exchangerates (basecurrencyid, targetcurrencyid, rate) VALUES (?,?,?)",
                exchange.getBaseCurrencyId(),
                exchange.getTargetCurrencyId(),
                exchange.getRate());
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
                getCurrencyByCode(Utils.USD).getId(),
                getCurrencyByCode(baseCurr).getId(),
                getCurrencyByCode(targetCurr).getId());
        return request.size() != 2 ? null : request;
    }
}
