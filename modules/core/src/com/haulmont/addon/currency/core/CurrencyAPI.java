package com.haulmont.addon.currency.core;


import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.entity.CurrencyRate;
import com.haulmont.addon.currency.entity.CurrencyAddonValue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CurrencyAPI {

    String NAME = "curraddon_CurrencyRateAPI";

    List<Currency> getAvailableCurrencies();

    BigDecimal convertAmount(BigDecimal amount, Date date, Currency currency, Currency targetCurrency);

    BigDecimal convertAmountToCurrentRate(BigDecimal amount, Currency currency, Currency targetCurrency);

    BigDecimal convertAmount(CurrencyAddonValue currencyValue, Currency targetCurrency);

    Currency getCurrencyByCode(String code);

    /**
     * Example:
     * convertAmount(USD, GBP) used 0.720940 rate
     * convertAmount(GBP, USD) used 1.387100 rate
     * But 1 / 0.720940 == 1.387077981524121 != 1.387100
     * In order to use the same rate (1.387077981524121) use convertAmountToRateReverse()
     */
    BigDecimal convertAmountToRateReverse(BigDecimal amount, Date date, Currency currency, Currency targetCurrency);

    CurrencyRate getLocalRate(Date date, Currency currency, Currency targetCurrency);
}
