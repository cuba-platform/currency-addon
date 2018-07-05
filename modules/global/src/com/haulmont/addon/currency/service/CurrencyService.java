package com.haulmont.addon.currency.service;


import com.haulmont.addon.currency.entity.Currency;

import java.math.BigDecimal;
import java.util.Date;

public interface CurrencyService {
    String NAME = "curraddon_CurrencyService";

    BigDecimal convertAmountToRate(BigDecimal amount, Date date, Currency currency, Currency targetCurrency);

    BigDecimal convertAmountToCurrentRate(BigDecimal amount, Currency currency, Currency targetCurrency);

    Currency getCurrencyByCode(String code);
    
}