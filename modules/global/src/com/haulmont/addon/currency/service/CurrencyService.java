package com.haulmont.addon.currency.service;


import com.haulmont.addon.currency.entity.CurrencyDescriptor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CurrencyService {
    String NAME = "curraddon_CurrencyService";

    BigDecimal convertAmountToRate(BigDecimal amount, Date date, CurrencyDescriptor currency, CurrencyDescriptor targetCurrency);

    BigDecimal convertAmountToCurrentRate(BigDecimal amount, CurrencyDescriptor currency, CurrencyDescriptor targetCurrency);

    CurrencyDescriptor getCurrencyByCode(String code);

    List<CurrencyDescriptor> getAvailableCurrencies();

    CurrencyDescriptor getDefaultCurrency();

}