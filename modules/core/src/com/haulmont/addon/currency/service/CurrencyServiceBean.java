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
    public ConvertResult convertAmountToRate(BigDecimal sourceAmount, Date exchangeDate, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency) {
        return currencies.convertAmount(sourceAmount, exchangeDate, sourceCurrency, targetCurrency);
    }


    @Override
    @Transactional
    public ConvertResult convertAmountToCurrentRate(BigDecimal sourceAmount, CurrencyDescriptor sourceCurrency, CurrencyDescriptor targetCurrency) {
        return currencies.convertAmountToCurrentRate(sourceAmount, sourceCurrency, targetCurrency);
    }


    @Override
    @Transactional
    public CurrencyDescriptor getCurrencyByCode(String currencyCode) {
        return currencies.getCurrencyByCode(currencyCode);
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