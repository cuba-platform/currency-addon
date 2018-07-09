package com.haulmont.addon.currency.core;


import com.haulmont.addon.currency.entity.Currency;
import com.haulmont.addon.currency.entity.CurrencyRate;

import java.util.Date;
import java.util.List;

public interface CurrencyRateProvider {

    String NAME = "curraddon_CurrencyRateProvider";

    List<CurrencyRate> getRates(Date date, Currency currency, List<Currency> targetCurrencies) throws Exception;
}
