package com.haulmont.addon.currency.service;

import com.haulmont.addon.currency.core.CurrencyAPI;
import com.haulmont.addon.currency.entity.Currency;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service(CurrencyService.NAME)
public class CurrencyServiceBean implements CurrencyService {

    @Inject
    private CurrencyAPI currencyApi;


    @Override
    @Transactional
    public BigDecimal convertAmountToRate(BigDecimal amount, Date date, Currency currency, Currency targetCurrency) {
        return currencyApi.convertAmount(amount, date, currency, targetCurrency);
    }


    @Override
    @Transactional
    public BigDecimal convertAmountToCurrentRate(BigDecimal amount, Currency currency, Currency targetCurrency) {
        return currencyApi.convertAmountToCurrentRate(amount, currency, targetCurrency);
    }


    @Override
    @Transactional
    public Currency getCurrencyByCode(String code) {
        return currencyApi.getCurrencyByCode(code);
    }


    @Override
    public List<Currency> getAvailableCurrencies() {
        return currencyApi.getActiveCurrencies();
    }


    @Override
    public Currency getDefaultCurrency() {
        return currencyApi.getDefaultCurrency();
    }

}