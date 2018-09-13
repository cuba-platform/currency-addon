package com.haulmont.addon.currency.service;

import com.haulmont.addon.currency.core.Currencies;
import com.haulmont.addon.currency.entity.CurrencyDescriptor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service(CurrencyService.NAME)
public class CurrencyServiceBean implements CurrencyService {

    @Inject
    private Currencies currencies;


    @Override
    @Transactional
    public BigDecimal convertAmountToRate(BigDecimal amount, Date date, CurrencyDescriptor currency, CurrencyDescriptor targetCurrency) {
        return currencies.convertAmount(amount, date, currency, targetCurrency);
    }


    @Override
    @Transactional
    public BigDecimal convertAmountToCurrentRate(BigDecimal amount, CurrencyDescriptor currency, CurrencyDescriptor targetCurrency) {
        return currencies.convertAmountToCurrentRate(amount, currency, targetCurrency);
    }


    @Override
    @Transactional
    public CurrencyDescriptor getCurrencyByCode(String code) {
        return currencies.getCurrencyByCode(code);
    }


    @Override
    public List<CurrencyDescriptor> getAvailableCurrencies() {
        return currencies.getActiveCurrencies();
    }


    @Override
    public CurrencyDescriptor getDefaultCurrency() {
        return currencies.getDefaultCurrency();
    }

}